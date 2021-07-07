package com.xlj.tools.convert;

import cn.hutool.core.util.EscapeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * XSS处理json入参的转义
 *
 * @author xlj
 * @date 2021/7/7
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {
    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        if (value != null) {
            return EscapeUtil.escape(value);
        }
        return value;
    }
}
