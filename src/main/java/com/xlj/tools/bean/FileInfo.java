package com.xlj.tools.bean;

import lombok.Data;

/**
 * 文件信息
 *
 * @author xlj
 * @date 2021/5/1 8:06
 */
@Data
public class FileInfo {
    private Integer id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
}
