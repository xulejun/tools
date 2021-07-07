package com.xlj.tools.component;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * XSS过滤处理
 *
 * @author xlj
 * @date 2021/7/7
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     * 构造一个包装给定请求的请求对象
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return EscapeUtil.escape(value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return EscapeUtil.escape(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = EscapeUtil.escape(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    /**
     * 重写getInputStream，对json格式参数进行过滤（也就是@RequestBody类型的参数）
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            return super.getInputStream();
        }

        // 为空，直接返回
        String json = IoUtil.read(super.getInputStream(), "utf-8");
        if (StrUtil.isEmpty(json)) {
            return super.getInputStream();
        }
        // 这里要注意，json格式的参数不能直接使用hutool的EscapeUtil.escape, 因为它会把"也给转义，
        // 使得@RequestBody没办法解析成为一个正常的对象，所以我们自己实现一个过滤方法
        // 或者采用定制自己的objectMapper处理json出入参的转义(推荐使用)
//        json = cleanXSS(json).trim();
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }
}
