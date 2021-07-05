package com.xlj.tools.wechat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信公众号文章
 *
 * @author xlj
 * @date 2021/3/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatArticle implements Serializable {
    private static final long serialVersionUID = -2834477448119860567L;
    /**
     * 文章链接
     */
    private String link;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 时间
     */
    private Long articleTime;
    /**
     * 正文
     */
    private String content;

}
