package com.xlj.tools.clickhouse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author legend xu
 * @date 2022/9/22
 */
public class ClickHouseHelpImpl implements ClickHouseHelp {
    private static Logger log = LoggerFactory.getLogger(ClickHouseHelpImpl.class);

    private Connection getConn(Class<?> clazz) throws Exception {
        DataSource dataSource = null;
        // clusterName = 库名_dalcluster
        String clusterName = clazz != null ? clazz.getAnnotation(ClickHouseEntity.class).clusterName() + "_dalcluster"
                : "htlcrawleroctopus_dbchdb_dalcluster";
        // String clusterName = "htlcrawleroctopus_dbchdb_dalcluster";
//        dataSource = new DalDataSourceFactory().getOrCreateDataSource(clusterName);
        return dataSource.getConnection();
    }

    @Override
    public boolean delete(Class<?> clazz, String condition) throws Exception {
        if (StringUtils.isEmpty(condition)) {
            log.warn("SQL删除语句条件不能为空");
            return false;
        }
        ClickHouseEntity annotation = clazz.getAnnotation(ClickHouseEntity.class);
        if (annotation == null) {
            return false;
        }
        String tableName = annotation.tableName();
        String sql = MessageFormat.format("ALTER TABLE {0} DELETE {1}", tableName, condition);
        log.info("ClickHouse 删除语句 SQL ={}", sql);
        try (Connection conn = getConn(clazz);
             PreparedStatement prepareStatement = conn.prepareStatement(sql)) {
            return prepareStatement.execute();
        } catch (Exception e) {
            log.error("ClickHouse 数据源连接异常：", e);
            return false;
        }
    }

    @Override
    public int[] batchInsert(List<?> entityType) throws Exception {
        Class<?> clazz = entityType.get(0).getClass();
        ClickHouseEntity annotation = clazz.getAnnotation(ClickHouseEntity.class);
        if (annotation == null) {
            return new int[0];
        }
        String tableName = annotation.tableName();
        // 全实体类字段
        List<ClickHouseColInfo> clickHouseColInfos = getClickHouseColumns(clazz, null);
        // 实体类字段拼接
        String columnSnippet = getClickHouseColumnSnippet(clickHouseColInfos);
        // 占位符拼接
        StringBuilder placeholderSnippet = new StringBuilder();
        for (int i = 0; i < clickHouseColInfos.size(); i++) {
            placeholderSnippet.append(i != clickHouseColInfos.size() - 1 ? "?, " : "?");
        }
        String sql = MessageFormat.format("INSERT INTO {0} ({1}) VALUES ({2})", tableName, columnSnippet, placeholderSnippet.toString());
        // 获取连接
        int[] result;
        try (Connection conn = getConn(clazz); PreparedStatement prepareStatement = conn.prepareStatement(sql)) {
            for (Object obj : entityType) {
                for (int i = 0; i < clickHouseColInfos.size(); i++) {
                    clickHouseColInfos.get(i).getField().setAccessible(true);
                    Object value = clickHouseColInfos.get(i).getField().get(obj);
                    prepareStatement.setObject(i + 1, value);
                }
                prepareStatement.addBatch();
            }
            result = prepareStatement.executeBatch();
            prepareStatement.clearBatch();
        } catch (Exception e) {
            log.error("ClickHouse 批量插入异常：", e);
            return new int[0];
        }
        return result;
    }

    @Override
    public <T> List<T> loadBySql(Class<T> entityType, List<String> cols, String sql) {
        List<T> result = new ArrayList<>();
        Connection conn;
        try {
            conn = getConn(entityType);
        } catch (Exception e) {
            log.error("ClickHouse 数据源连接异常：", e);
            return result;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<ClickHouseColInfo> clickHouseColumns = getClickHouseColumns(entityType, cols, null);
                Map<String, ClickHouseColInfo> columnMap = this.getColMap(clickHouseColumns);
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    T entity = entityType.newInstance();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String colName = metaData.getColumnName(i);
                        if (colName == null) {
                            continue;
                        }
                        ClickHouseColInfo clickHouseColumn = columnMap.get(colName.toLowerCase());
                        if (clickHouseColumn == null) {
                            continue;
                        }
                        clickHouseColumn.set(entity, clickHouseColumn.load(rs.getObject(i)));
                    }
                    result.add(entity);
                }
            }
        } catch (Exception e) {
            log.error("sql={} 异常：", sql, e);
        } finally {
            closeConn(conn);
        }
        return result;
    }

    @Override
    public <T> List<T> loadByCondition(Class<T> entityType, List<String> cols, String condition, Object... params) {
        List<T> result = new ArrayList<>();
        List<ClickHouseColInfo> clickHouseColumns = getClickHouseColumns(entityType, cols, null);
        final String sql = String.format("SELECT %s FROM %s%s", getClickHouseColumnSnippet(clickHouseColumns)
                , entityType.getAnnotation(ClickHouseEntity.class).tableName(), condition == null ? "" : " WHERE " + condition);
        log.info("ClickHouse 查询语句 SQL = {}", sql);
        Connection conn;
        try {
            conn = getConn(entityType);
        } catch (Exception e) {
            log.error("clickhouse 数据源连接异常：", e);
            return result;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(1 + i, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, ClickHouseColInfo> columnMap = this.getColMap(clickHouseColumns);
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    T entity = entityType.newInstance();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String colName = metaData.getColumnName(i);
                        ClickHouseColInfo clickHouseColumn = columnMap.get(colName.toLowerCase());
                        if (clickHouseColumn == null) {
                            continue;
                        }
                        clickHouseColumn.set(entity, clickHouseColumn.load(rs.getObject(i)));
                    }
                    result.add(entity);
                }
            }
        } catch (Exception e) {
            log.error("sql={} 异常：", sql, e);
        } finally {
            closeConn(conn);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> loadBySql(String sql) {
        List<Map<String, Object>> results = new ArrayList<>();
        Connection conn;
        try {
            conn = getConn(null);
        } catch (Exception e) {
            log.error("clickhouse 数据源连接异常：", e);
            return null;
        }
        try (Statement statement = conn.createStatement();) {
            try (ResultSet result = statement.executeQuery(sql)) {
                ResultSetMetaData metaData = result.getMetaData();
                while (result.next()) {
                    Map<String, Object> rowMap = new HashMap<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String colName = metaData.getColumnName(i);
                        Object value = result.getObject(i);
                        rowMap.put(colName, value);
                    }
                    results.add(rowMap);
                }
            }
        } catch (Exception e) {
            log.error(sql, e);
        } finally {
            closeConn(conn);
        }
        return results;
    }


    @Override
    public <T> List<T> loadAll(Class<T> entityType, String partitionDate) {
        return loadByCondition(entityType, StringUtils.isBlank(partitionDate) ? null : "d='" + partitionDate + "'");
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityType, List<String> cols, String partitionDate) {
        return loadByCondition(entityType, cols, StringUtils.isBlank(partitionDate) ? null : "d='" + partitionDate + "'");
    }

    @Override
    public <T> List<T> loadLatest(Class<T> entityType, String partitionDate) {
        String d = StringUtils.isBlank(partitionDate) ? "" : "d='" + partitionDate + "'";
        return loadByCondition(entityType, d);
    }

    @Override
    public <T> List<T> loadLatest(Class<T> entityType, List<String> cols, String partitionDate) {
        String d = StringUtils.isBlank(partitionDate) ? "" : "d='" + partitionDate + "'";
        return loadByCondition(entityType, cols, d);
    }

    @Override
    public <T> List<T> loadByCondition(Class<T> entityType, String condition, Object... params) {
        return loadByCondition(entityType, null, condition, params);
    }

    @Override
    public <T> List<T> loadBySql(Class<T> entityType, String sql) {
        return loadBySql(entityType, null, sql);
    }

    /**
     * 获取实体类部分指定字段
     *
     * @param clazz
     * @param clos
     * @param idClickHouseColumn
     * @return
     */
    private List<ClickHouseColInfo> getClickHouseColumns(Class<?> clazz, List<String> clos, ClickHouseColInfo[] idClickHouseColumn) {
        if (clos == null || clos.size() == 0) {
            return getClickHouseColumns(clazz, idClickHouseColumn);
        }
        convertColumnLowercase(clos);
        List<ClickHouseColInfo> columnList = new ArrayList<>();
        if (clazz.getSuperclass() != null) {
            columnList.addAll(getClickHouseColumns(clazz.getSuperclass(), clos, idClickHouseColumn));
        }
        for (Field field : clazz.getDeclaredFields()) {
            ClickHouseColumn annot = field.getAnnotation(ClickHouseColumn.class);
            if (annot != null) {
                ClickHouseColInfo clickHouseColInfo = new ClickHouseColInfo(field, annot);
                if (clos.contains(clickHouseColInfo.getName().toLowerCase())) {
                    columnList.add(clickHouseColInfo);
                }
                if (field.getAnnotation(ClickHouseId.class) != null && idClickHouseColumn != null && idClickHouseColumn.length > 0) {
                    idClickHouseColumn[0] = clickHouseColInfo;
                }
            }
        }
        return columnList;
    }

    /**
     * 获取实体类全字段
     *
     * @param clazz
     * @param idClickHouseColumn
     * @return
     */
    private List<ClickHouseColInfo> getClickHouseColumns(Class<?> clazz, ClickHouseColInfo[] idClickHouseColumn) {
        List<ClickHouseColInfo> columnList = new ArrayList<>();
        if (clazz.getSuperclass() != null) {
            columnList.addAll(getClickHouseColumns(clazz.getSuperclass(), idClickHouseColumn));
        }
        for (Field field : clazz.getDeclaredFields()) {
            ClickHouseColumn annot = field.getAnnotation(ClickHouseColumn.class);
            if (annot != null) {
                ClickHouseColInfo clickHouseColInfo = new ClickHouseColInfo(field, annot);
                columnList.add(clickHouseColInfo);
                if (field.getAnnotation(ClickHouseId.class) != null && idClickHouseColumn != null && idClickHouseColumn.length > 0) {
                    idClickHouseColumn[0] = clickHouseColInfo;
                }
            }
        }
        return columnList;
    }

    /**
     * 实体类字段拼接
     *
     * @param columnList
     * @return
     */
    private String getClickHouseColumnSnippet(List<ClickHouseColInfo> columnList) {
        StringBuilder snippet = new StringBuilder();
        for (int i = 0; i < columnList.size(); i++) {
            snippet.append(columnList.get(i).getName());
            if (i < columnList.size() - 1) {
                snippet.append(", ");
            }
        }
        return snippet.toString();
    }

    /**
     * 获取实体类字段每个值
     *
     * @param obj
     * @return
     */
    private String getColumnValueSnippet(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        StringBuilder snippet = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 获取每个字段 get 方法值
            ClickHouseColumn annotation = fields[i].getAnnotation(ClickHouseColumn.class);
            if (annotation != null) {
                String methodName = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
                Object value = clazz.getMethod(methodName).invoke(clazz);
                snippet.append(value);
            }
            // , 分隔
            if (i < fields.length - 1) {
                snippet.append("', '");
            }
        }
        return snippet.toString();
    }

    private Map<String, ClickHouseColInfo> getColMap(List<ClickHouseColInfo> list) {
        Map<String, ClickHouseColInfo> map = new HashMap<>();
        list.forEach(hc -> {
            map.put(hc.getName().toLowerCase(), hc);
        });
        return map;
    }

    private void convertColumnLowercase(List<String> clos) {
        if (clos == null || clos.isEmpty()) {
            return;
        }
        for (int i = 0; i < clos.size(); i++) {
            clos.set(i, clos.get(i).toLowerCase());
        }
    }

    private void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.warn("sql 关闭连接异常：", e);
            }
        }
    }
}
