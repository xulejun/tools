package com.xlj.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

//@SpringBootTest
public class ToolsApplicationTests {

    @Test
    void contextLoads() throws IOException {
        File file = new File("");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
        System.out.println(File.separator);
    }

}
