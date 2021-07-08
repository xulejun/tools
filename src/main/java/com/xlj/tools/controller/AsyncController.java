package com.xlj.tools.controller;

import com.xlj.tools.service.AsyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异步调用：控制层
 *
 * @author xlj
 * @date 2021/7/8
 */
@Slf4j
@RestController
public class AsyncController {
    @Autowired
    private AsyncTaskService asyncTaskService;

    @GetMapping("/async")
    public String async(@RequestParam Integer param){
        asyncTaskService.asyncTask1(param);
        asyncTaskService.asyncTask2(param);
        return "异步方法调用完成";
    }
}
