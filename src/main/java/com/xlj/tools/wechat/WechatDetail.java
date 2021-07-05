package com.xlj.tools.wechat;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xlj
 * @date 2021/4/14
 */
@Slf4j
public class WechatDetail {
    private static final String ATTR_STYLE = "style";

    /**
     * 正文采集过滤
     *
     * @return 公众号文章正文内容
     * @throws IOException
     */
    public static void articleFilter(WechatArticle article) throws Exception {
        // 标题和时间都可通过列表页获取
        String url = article.getLink();
        Document doc = Jsoup.connect(url).maxBodySize(0).timeout(15 * 1000).get();

        if (Objects.isNull(doc)) {
            return;
        }

        Element contentElement = doc.getElementById("js_content");
        // 除去 style="visibility: hidden;"   否则文章无法显示
        contentElement.attr(ATTR_STYLE, "");
        // 转载未设置白名单过滤
        String jsShare = "js_share_content";
        if (doc.getElementById(jsShare) != null) {
//            article.setContent(CollectCodeEnum.NOT_WHITELIST.getDesc());
            return;
        }
        // 图片过滤处理
        imgFilter(contentElement);

        // 音频过滤
        contentElement.select("mpvoice,qqmusic").forEach(element -> element.parent().remove());
        // 视频过滤
        contentElement.select("iframe.video_iframe").forEach(Node::remove);
        contentElement.select("#js_mpvedio").forEach(Node::remove);
        // 视频，广告过滤
        contentElement.select("mpvideosnap,mpcps").forEach(Node::remove);

        article.setContent(contentElement.outerHtml());
    }

    /**
     * 图片过滤处理
     *
     * @param contentElement
     */
    private static void imgFilter(Element contentElement) {
        String img = "img";
        contentElement.select("a").forEach(element -> {
            // a标签的父标签的中文内容与a标签的中文内容相同，就是列表超链
            if (isATagList(element)) {
                element.remove();
                return;
            }
            Elements childers = element.getAllElements();
            Element lastChilder = childers.get(childers.size() - 1);

            // a标签最后子标签为img标签就剔除，就判定它是图片超链
            if (img.equals(lastChilder.tagName())) {
                element.remove();
            }
        });

        String dataSrc = "data-src";
        String gif = "gif";
        String dataType = "data-type";
        String imageLink = "js_jump_icon h5_image_link";
        contentElement.select(img).forEach(element -> {
            String imgUrl = element.attr(dataSrc);
            // gif图片过滤
            boolean isGif = gif.equals(element.attr(dataType)) || gif.equals(imgUrl.substring(imgUrl.lastIndexOf("=") + 1));
            if (isGif) {
                removeGifAndParents(element);
                return;
            }
            // 图片外链过滤
            if (element.parent() != null && imageLink.equals(element.parent().attr("class"))) {
                element.parent().remove();
                return;
            }
            // 微信二维码、小程序过滤
//            if (StringUtils.isNotBlank(QrCodeUtil.deEncodeByUrl(imgUrl))) {
//                element.remove();
//            }
            // 将data-src属性置换成src，否则页面显示不出该图片
            element.removeAttr(dataSrc).attr("src", imgUrl);
            // 图片大小设置最大100%宽度
            final String style = element.attr(ATTR_STYLE);
            if (StrUtil.isBlank(style)) {
                element.attr(ATTR_STYLE, "max-width:100%");
            } else {
                element.attr(ATTR_STYLE, style + ";max-width:100%");
            }
        });
    }

    /**
     * 判断是否为列表超链
     *
     * @param element
     * @return
     */
    private static boolean isATagList(Element element) {
        // a标签的父标签是否为块级标签 && 中文内容 和 a标签的内容是否一致
        for (Element parent : element.parents()) {
            if (parent.isBlock() && rexChinese(parent.text()).equals(rexChinese(element.text()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 正则匹配中文
     *
     * @param text
     * @return
     */
    private static String rexChinese(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        // 按指定模式在字符串查找
        String pattern = "[\\u4e00-\\u9fa5]";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(text);
        while (m.find()) {
            stringBuilder.append(m.group(0));
        }
        return stringBuilder.toString().replace("年月日", "");
    }

    private static void removeGifAndParents(Element img) {
        if (img == null) {
            return;
        }
        final Elements parents = img.parents();
        img.remove();
        parents.forEach(parent -> {
            if (StringUtils.isBlank(parent.html())) {
                parent.remove();
            }
        });
    }
}
