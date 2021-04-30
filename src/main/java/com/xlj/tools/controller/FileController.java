package com.xlj.tools.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件控制层
 *
 * @author xlj
 * @date 2021/4/30
 */
@Controller
public class FileController {
    @GetMapping("/listFile")
    public String listFile() {
        return "listFile";
    }

    /**
     * 图片上传
     *
     * @param imgFile
     * @throws IOException
     */
    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        // 随机文件名
        String randomString = RandomUtil.randomString(10);
        String fileName = randomString.concat(".jpg");
        // 文件上传路径
        String filePath = "D:\\XLJ\\Idea-workspace\\tools\\src\\main\\resources\\img";
        File newFile = new File(filePath, fileName);
        newFile.getParentFile().mkdir();
        // 文件上传
        imgFile.transferTo(newFile);
        return "listFile";
    }
}
