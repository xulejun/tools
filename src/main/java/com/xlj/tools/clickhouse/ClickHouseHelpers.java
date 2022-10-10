package com.xlj.tools.clickhouse;


import java.util.List;
import java.util.Map;

public class ClickHouseHelpers {
    private static ClickHouseHelp dao = null;

    static {
        setHiveQuery(new ClickHouseHelpImpl());
    }

    public static void setHiveQuery(ClickHouseHelp hiveQuery) {
        dao = hiveQuery;
    }

    /**
     * 1不推荐使用，除非全字段都需要使用。个别字段的话请使用loadAll(entityType, cols, partitionDate)
     * partitionDate: yyyy-MM-dd
     **/
    @Deprecated
    public static <T> List<T> loadAll(Class<T> entityType, String partitionDate) {
        return dao == null ? null : dao.loadAll(entityType, partitionDate);
    }

    /**
     * partitionDate: yyyy-MM-dd
     *
     * @param <T>
     * @param entityType
     * @param cols
     * @param partitionDate
     * @return
     */

    public static <T> List<T> loadAll(Class<T> entityType, List<String> cols,
                                      String partitionDate) {
        return dao == null ? null : dao.loadAll(entityType, cols, partitionDate);
    }

    /**
     * 1不推荐使用，除非全字段都需要使用。个别字段的话请使用loadLatest(entityType, cols, partitionDate)
     * partitionDate: yyyy-MM-dd
     **/
    @Deprecated
    public static <T> List<T> loadLatest(Class<T> entityType, String partitionDate) {
        return dao == null ? null : dao.loadLatest(entityType, partitionDate);
    }

    /**
     * partitionDate: yyyy-MM-dd
     *
     * @param <T>
     * @param entityType
     * @param cols
     * @param partitionDate
     * @return
     */
    public static <T> List<T> loadLatest(Class<T> entityType, List<String> cols,
                                         String partitionDate) {
        return dao == null ? null : dao.loadLatest(entityType, cols, partitionDate);
    }

    /**
     * 1不推荐使用，除非全字段都需要使用。个别字段的话请使用loadByCondition(Class<T>
     * entityType,List<String> cols, String condition, Object... params)
     **/
    @Deprecated
    public static <T> List<T> loadByCondition(Class<T> entityType, String condition,
                                              Object... params) {
        return dao == null ? null : dao.loadByCondition(entityType, condition, params);
    }

    public static <T> List<T> loadByCondition(Class<T> entityType, List<String> cols,
                                              String condition, Object... params) {
        return dao == null ? null : dao.loadByCondition(entityType, cols, condition, params);
    }

    /**
     * 1不推荐使用，除非全字段都需要使用。个别字段的话请使用loadBySQL(Class<T> entityType, String sql)
     **/
    @Deprecated
    public static <T> List<T> loadBySql(Class<T> entityType, String sql) {
        return dao == null ? null : dao.loadBySql(entityType, sql);
    }

    public static <T> List<T> loadBySql(Class<T> entityType, List<String> cols, String sql) {
        return dao == null ? null : dao.loadBySql(entityType, cols, sql);
    }

    public static List<Map<String, Object>> loadBySql(String sql) {
        return dao == null ? null : dao.loadBySql(sql);
    }

    public static int[] batchInsert(List<?> entityType) throws Exception {
        return dao == null ? null : dao.batchInsert(entityType);
    }

    public static boolean delete(Class<?> clazz, String condition) throws Exception {
        if (dao == null) {
            return false;
        }
        return dao.delete(clazz, condition);
    }
}
