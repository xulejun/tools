package com.xlj.tools.clickhouse;

import java.util.List;
import java.util.Map;

/**
 * @author legend xu
 * @date 2022/9/22
 */
public interface ClickHouseHelp {
    public <T> List<T> loadAll(Class<T> entityType, String partitionDate);

    public <T> List<T> loadAll(Class<T> entityType, List<String> cols, String partitionDate);

    public <T> List<T> loadLatest(Class<T> entityType, String partitionDate);

    public <T> List<T> loadLatest(Class<T> entityType, List<String> cols, String partitionDate);

    public <T> List<T> loadByCondition(Class<T> entityType, String condition, Object... params);

    public <T> List<T> loadByCondition(Class<T> entityType, List<String> cols, String condition, Object... params);

    public <T> List<T> loadBySql(Class<T> entityType, List<String> cols, String sql);

    public <T> List<T> loadBySql(Class<T> entityType, String sql);

    public List<Map<String, Object>> loadBySql(String sql);

    public int[] batchInsert(List<?> entityType) throws Exception;

    public boolean delete(Class<?> clazz, String condition) throws Exception;
}
