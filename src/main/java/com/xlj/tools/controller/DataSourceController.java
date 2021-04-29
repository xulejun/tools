package com.xlj.tools.controller;

import com.xlj.tools.dao.one.DatasourceOne;
import com.xlj.tools.dao.two.DatasourceTwo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author XLJ
 * @date 2020/9/22
 */
@RestController
public class DataSourceController {
    @Resource
    private DatasourceOne datasourceOne;

    @Resource
    private DatasourceTwo datasourceTwo;

    @GetMapping(value = "/insertOne")
    public void insertOne(){
        datasourceOne.insert();
    }

    @GetMapping(value = "/insertTwo")
    public void insertTwo(){
        datasourceTwo.insert();
    }
}
