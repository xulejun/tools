package com.xlj.tools.dao;


import com.xlj.tools.bean.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/***
 * @description 文件信息
 * @author xlj
 * @date 2021/5/1 18:34
 */
@Mapper
public interface FileInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    List<FileInfo> selectAll();
}