package com.xlj.tools.dao.two;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author XLJ
 * @date 2020/9/22
 */
@Mapper
public interface DatasourceTwo {
    @Insert(" insert into data_test (name) values (\"xlj\")")
    void insert();
}
