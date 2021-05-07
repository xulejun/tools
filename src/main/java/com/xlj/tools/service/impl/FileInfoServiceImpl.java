package com.xlj.tools.service.impl;

import com.xlj.tools.bean.FileInfo;
import com.xlj.tools.dao.FileInfoDao;
import com.xlj.tools.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xlj
 * @date 2021/5/1 18:37
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return fileInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FileInfo fileInfo) {
        return fileInfoDao.insert(fileInfo);
    }

    @Override
    public int insertSelective(FileInfo fileInfo) {
        return fileInfoDao.insertSelective(fileInfo);
    }

    @Override
    public FileInfo selectByPrimaryKey(Integer id) {
        return fileInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FileInfo fileInfo) {
        return fileInfoDao.updateByPrimaryKeySelective(fileInfo);
    }

    @Override
    public int updateByPrimaryKey(FileInfo fileInfo) {
        return fileInfoDao.updateByPrimaryKey(fileInfo);
    }

    @Override
    public List<FileInfo> selectAll() {
        return fileInfoDao.selectAll();
    }
}
