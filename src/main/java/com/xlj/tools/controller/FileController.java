package com.xlj.tools.controller;

import cn.hutool.core.util.RandomUtil;
import com.xlj.tools.bean.FileInfo;
import com.xlj.tools.service.FileInfoService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 文件控制层
 *
 * @author xlj
 * @date 2021/4/30
 */
@Controller
public class FileController {
    @Autowired
    private FileInfoService fileInfoService;

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
        String suffixPath = "\\src\\main\\resources\\img";
        // 文件上传路径
        String filePath = new File("").getAbsolutePath().concat(suffixPath);
        File newFile = new File(filePath, fileName);
        newFile.getParentFile().mkdir();
        // 文件上传
        imgFile.transferTo(newFile);
        return "listFile";
    }

    /**
     * 图片下载
     *
     * @param fileId
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping
    public ResponseEntity<byte[]> download(Integer fileId) throws IOException, SQLException {
        FileInfo fileInfo = fileInfoService.selectByPrimaryKey(fileId);
        String fileName = fileInfo.getName();
        String fileUrl = fileInfo.getPath();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<byte[]> entity =
                new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(fileUrl)), headers, HttpStatus.CREATED);
        if (entity == null) {
            return null;
        } else {
            return entity;
        }
    }
}
