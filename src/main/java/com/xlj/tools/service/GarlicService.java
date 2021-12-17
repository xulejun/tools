package com.xlj.tools.service;

import com.xlj.tools.bean.Garlic;

import java.util.List;

/**
 * @author xlj
 * @date 2021/4/29
 */
public interface GarlicService {
    int deleteByPrimaryKey(Integer id);

    int insert(Garlic record);

    int insertSelective(Garlic record);

    Garlic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Garlic record);

    int updateByPrimaryKey(Garlic record);
}
