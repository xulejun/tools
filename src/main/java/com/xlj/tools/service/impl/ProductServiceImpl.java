package com.xlj.tools.service.impl;

import com.xlj.tools.bean.Product;
import com.xlj.tools.dao.ProductDao;
import com.xlj.tools.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return productDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Product product) {
        return productDao.insert(product);
    }

    @Override
    public int insertSelective(Product product) {
        return productDao.insertSelective(product);
    }

    @Override
    public Product selectByPrimaryKey(Integer id) {
        return productDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Product product) {
        return productDao.updateByPrimaryKeySelective(product);
    }

    @Override
    public int updateByPrimaryKey(Product product) {
        return productDao.updateByPrimaryKeySelective(product);
    }

    @Override
    public List<Product> selectAll() {
        return productDao.selectAll();
    }

    @Override
    public List<Product> selectSkillProduct() {
        return productDao.selectSkillProduct();
    }
}
