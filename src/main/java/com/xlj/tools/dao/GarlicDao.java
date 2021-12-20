package com.xlj.tools.dao;


import com.xlj.tools.bean.Garlic;

/**
 * @author lejunxu
 */
public interface GarlicDao {

    int deleteByPrimaryKey(Integer id);

    int insert(Garlic record);

    int insertSelective(Garlic record);

    Garlic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Garlic record);

    int updateByPrimaryKey(Garlic record);

    int replace(Garlic garlic);
}




