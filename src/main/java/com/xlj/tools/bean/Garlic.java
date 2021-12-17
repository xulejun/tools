package com.xlj.tools.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * 大蒜行情价格
 *
 * @author lejunxu
 * @TableName garlic
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Garlic implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 详情Url
     */
    private String detailUrl;

    /**
     * 文章发布时间
     */
    private String articleTime;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 文章采集时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date crawlerTime;

    private static final long serialVersionUID = 1L;
}