package com.xlj.tools.controller;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlj.tools.bean.FileInfo;
import com.xlj.tools.service.FileInfoService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    public String listFile(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                           @RequestParam(value = "size", defaultValue = "5") int size) {
        // 分页
        PageHelper.startPage(start, size);
        List<FileInfo> fileInfos = fileInfoService.selectAll();
        PageInfo<FileInfo> files = new PageInfo<>(fileInfos);
        model.addAttribute("files", files);
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
        String path = new File("").getAbsolutePath().concat(suffixPath);
        File newFile = new File(path, fileName);
        newFile.getParentFile().mkdir();
        // 文件上传
        imgFile.transferTo(newFile);

        // 文件地址
        String filePath = path + "\\" + fileName;
        // 保存到数据库
        fileInfoService.insert(FileInfo.builder().name(fileName).path(filePath).build());
        return "redirect:listFile";
    }

    /**
     * 图片下载
     *
     * @param fileId
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam(value = "id") Integer fileId) throws IOException {
        FileInfo fileInfo = fileInfoService.selectByPrimaryKey(fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileInfo.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<byte[]> entity =
                new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(fileInfo.getPath())), headers, HttpStatus.CREATED);
        if (entity == null) {
            return null;
        } else {
            return entity;
        }
    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam(value = "id") int id) {
        fileInfoService.deleteByPrimaryKey(id);
        return "redirect:listFile";
    }
}
