package com.xlj.tools.basis.patterns.factory.simple.spring;

import org.springframework.stereotype.Service;

/**
 * @author legend xu
 * @date 2022/1/28
 */
@Service
public class Triangle implements Shape {
    @Override
    public void draw() {
        System.out.println("画一个三角形");
    }
}
