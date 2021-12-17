package com.xlj.tools.service.impl;

import com.xlj.tools.bean.Garlic;
import com.xlj.tools.dao.GarlicDao;
import com.xlj.tools.service.GarlicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Service
public class GarlicServiceImpl implements GarlicService {
    @Autowired
    private GarlicDao garlicDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return garlicDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Garlic garlic) {
        return garlicDao.insert(garlic);
    }

    @Override
    public int insertSelective(Garlic garlic) {
        return garlicDao.insertSelective(garlic);
    }

    @Override
    public Garlic selectByPrimaryKey(Integer id) {
        return garlicDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Garlic garlic) {
        return garlicDao.updateByPrimaryKeySelective(garlic);
    }

    @Override
    public int updateByPrimaryKey(Garlic garlic) {
        return garlicDao.updateByPrimaryKeySelective(garlic);
    }
}
