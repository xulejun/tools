package com.xlj.tools.controller;

import com.xlj.tools.util.CronExpressionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

/**
 * cron表达式实现定时
 *
 * @author xlj
 * @date 2021/7/6
 */
@RestController
public class CronController {
    @Autowired
    private CronExpressionUtil cronExpressionUtil;

    @GetMapping(value = "/cronStart/{id}/{cron}")
    public String cron(@PathVariable Long id, @PathVariable String cron) {
        cronExpressionUtil.start(id, cron);
        return MessageFormat.format("定时调度{0}开启", id);
    }

    @GetMapping(value = "/cronStop/{id}")
    public String cron(@PathVariable Long id) {
        cronExpressionUtil.stop(id);
        return MessageFormat.format("定时调度{0}关闭", id);
    }
}
