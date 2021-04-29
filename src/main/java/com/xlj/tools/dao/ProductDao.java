package com.xlj.tools.dao;

import com.xlj.tools.bean.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Mapper
public interface ProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectAll();
}
