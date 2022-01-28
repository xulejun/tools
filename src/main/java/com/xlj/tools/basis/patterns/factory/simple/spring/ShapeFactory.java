package com.xlj.tools.basis.patterns.factory.simple.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 使用 spring注解完成简单工厂模式
 *      1. 创建公有 接口 / 抽象类
 *      2. 多个实现类，将类注入到spring容器中（添加 service 注解）
 *      3. 创建工厂类，将实现类注入到map中
 *      4. 提供一个共有方法，外界通过实现类名（小写），在map中找到对应的实现类，即可调用不同实现方法
 * 通过这种方式，可减少大量if else 判断
 * @author legend xu
 * @date 2022/1/28
 */
@Component
public class ShapeFactory {
    @Autowired
    private Map<String, Shape> shapeMap;

    public Shape getShape(String shapeStr) {
        return shapeMap.get(shapeStr);
    }
}
