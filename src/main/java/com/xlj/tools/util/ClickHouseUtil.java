package com.xlj.tools.util;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * clickhouse 连接操作样例
 *
 * @author legend xu
 * @date 2022/7/7
 */
public class ClickHouseUtil {
    private static Logger logger = LoggerFactory.getLogger(ClickHouseUtil.class);
    private static Properties properties;
    private static ClickHouseDataSource dataSource;
    private static String url = "jdbc:ch:https://play.clickhouse.com:443/test?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";

    static {
        properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");
        try {
            dataSource = new ClickHouseDataSource(url, properties);
        } catch (SQLException e) {
            logger.warn("clickHouse 数据源连接异常，url={}，properties={}，", url, properties, e);
        }
    }

    public static void main(String[] args) throws Exception {
        String querySql = "select * from user";
        String deleteSql = "delete from user where id = '7'";
        String updateSql = "update user set name = 'xlj666' where id = '7'";
        String insertSql = "insert into user (name) values ('xlj520')";
        execute(insertSql);
        executeQuery(querySql);
    }

    /**
     * 批量插入数据（url 需要加 rewriteBatchedStatements=true，并且 commit 为手动提交 ）
     *
     * @param tableName 表名
     * @param objects   bean 列表
     * @return
     * @throws Exception
     */
    public static void batchInsert(String tableName, List<?> objects) throws Exception {
        // 批量插入 SQL 脚本拼接， 例：insert into table (id,name) value (1,xlj1),(2,xlj2)
        String sql = "insert into " + tableName;
        Field[] fields = objects.get(0).getClass().getDeclaredFields();
        Method[] methods = objects.get(0).getClass().getMethods();
        for (int i = 0; i < fields.length; i++) {
            if (i == 0) {
                sql = sql + " (" + fields[i].getName() + ",";
            } else if (i == fields.length - 1) {
                sql = sql + fields[i].getName() + ")";
            } else {
                sql = sql + fields[i].getName() + ",";
            }
        }
        sql = sql + " value ";
        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < fields.length; j++) {
                if (j == 0) {
                    sql = sql + "(?,";
                } else if (j == fields.length - 1 && i == objects.size() - 1) {
                    sql = sql + "?)";
                } else if (j == fields.length - 1) {
                    sql = sql + "?),";
                } else {
                    sql = sql + "?,";
                }
            }
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepareStatement = conn.prepareStatement(sql);) {
            // 取消自动提交 SQL
            conn.setAutoCommit(false);
            // 数据填充
            for (Object object : objects) {
                for (Method method : methods) {
                    for (int i = 0; i < fields.length; i++) {
                        if (method.getName().equalsIgnoreCase("get" + fields[i].getName())) {
                            // 通过反射实现方法调用
                            Object value = method.invoke(object);
                            prepareStatement.setObject(i, value);
                            prepareStatement.addBatch();
                            // 批量执行
                            if (i % 500 == 0) {
                                prepareStatement.executeBatch();
                                prepareStatement.clearBatch();
                            }
                        }
                    }
                }
            }
            prepareStatement.executeBatch();
            prepareStatement.clearBatch();
            conn.commit();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 增删改 SQL 执行
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static boolean execute(String sql) throws Exception {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepareStatement = conn.prepareStatement(sql);) {
            return prepareStatement.execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 查询 SQL
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static ResultSet executeQuery(String sql) throws Exception {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepareStatement = conn.prepareStatement(sql);) {
            ResultSet resultSet = prepareStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    System.out.println(metaData.getColumnName(i) + ":" + resultSet.getString(i));
                }
            }
            return resultSet;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 分批插入数据
     */
    public void batchInsert(){
        ArrayList<Object> list = Lists.newArrayList();
        for (int i = 0; i < 105; i++) {
            list.add("xlj" + i);
        }

        ArrayList<Object> tempList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            tempList.add(list.get(i));
            if (tempList.size() % 20 == 0) {
                System.out.println("执行插入：" + list.get(i));
                tempList.clear();
                continue;
            }
            if (i == list.size() - 1) {
                System.out.println("执行插入：" + list.get(i));
                tempList.clear();
            }
        }
    }
}
