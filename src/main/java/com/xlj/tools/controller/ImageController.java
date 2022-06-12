package com.xlj.tools.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * 图片上传并展示
 *
 * @author legend xu
 * @date 2022/6/12
 */
@Controller
@RequestMapping("/image")
public class ImageController {
    /**
     * 文件上传路径
     */
    private final String suffixPath = "\\src\\main\\resources\\static\\img";

    /**
     * 初始化列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model) {
        // 文件上传路径
        String path = new File("").getAbsolutePath().concat(suffixPath);
        // 获取文件夹下所有文件名
        ArrayList<String> paths = new ArrayList<>();
        File file = new File(path);
        File[] fileArr = file.listFiles();
        for (File f : Objects.requireNonNull(fileArr)) {
            if (f.isFile()) {
                paths.add(f.toString());
            }
        }
        model.addAttribute("paths", paths);

        return "image";
    }

    /**
     * 图片上传
     *
     * @param imgFile
     * @throws IOException
     */
    @PostMapping("/upload")
    public String uploadImg(@RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        // 随机文件名
        String randomString = UUID.randomUUID().toString();
        String fileName = randomString.concat(".jpg");
        // 文件上传路径
        String path = new File("").getAbsolutePath().concat(suffixPath);
        File newFile = new File(path, fileName);
        newFile.getParentFile().mkdir();
        // 文件上传
        imgFile.transferTo(newFile);

        return "redirect:/image/list";
    }

    /**
     * 图片下载
     *
     * @param filePath
     * @return
     */
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(String filePath) {
        try {
            // 设置编码
            String newFileName = new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            // 生成下载的文件
            File file = new File(filePath);
            // 设置HttpHeaders,使得浏览器响应下载
            HttpHeaders headers = new HttpHeaders();
            // 给浏览器设置下载文件名
            headers.setContentDispositionFormData("attachment", newFileName);
            // 浏览器响应方式
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 把文件以二进制格式响应给浏览器
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
