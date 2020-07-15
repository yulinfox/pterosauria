package com.toroto.pterosauria.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * json字符串 - 对象转换工具
 *
 * @author yulinfu
 * @date 2018/8/29
 */
@Slf4j
public class JsonUtil {

    private JsonUtil() {

    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象转为字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (null == obj) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("to json error: ###{}", e);
            throw new RuntimeException("to json error: ### " + e);
        }
    }

    /**
     * json字符串转换为对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clz);
        } catch (Exception e) {
            log.error("from json error: ###{}", e);
            throw new RuntimeException("from json error: ###" + e);
        }
    }

}
