package com.xlj.tools.service;

import com.xlj.tools.bean.FileInfo;

/**
 * @author XLJ
 */
public interface FileInfoService {
    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     *
     * @param record
     * @return
     */
    int insert(FileInfo record);

    /**
     * 选择插入
     *
     * @param record
     * @return
     */
    int insertSelective(FileInfo record);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    FileInfo selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(FileInfo record);

    /**
     * 根据主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(FileInfo record);
}
