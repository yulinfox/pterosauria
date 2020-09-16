package com.toroto.pterosauria.parser.processor;

import com.toroto.pterosauria.domain.RequestData;
import com.toroto.pterosauria.parser.AbstractParser;
import com.toroto.pterosauria.parser.DefaultParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
@Slf4j
public class ParseProcessor {

    private static final Map<String, AbstractParser> PARSER_MAP = new ConcurrentHashMap<>(4);

    private static AbstractParser DEFAULT_PARSER;

    public static final char PREFIX = '{';
    public static final char SUFFIX = '}';

    private static final String QUERY = "QUERY";
    private static final String BODY = "BODY";

    public static final void register(String placeHolder, AbstractParser parser) {
        PARSER_MAP.put(placeHolder, parser);
    }

    public static String parse(String template, RequestData data) {
        return getResponse(template, data);
    }

    @Autowired
    public void setDefaultParser(DefaultParser defaultParser) {
        DEFAULT_PARSER = defaultParser;
    }

    /**
     * 格式化返回数据
     * 支持自定义
     * @param template
     * @return
     */
    protected static String getResponse(final String template, RequestData data) {
        List<String> placeHolders = processPlaceHolder(template);
        // 占位符取值替换
        Map<String, String> replaceMap = getReplaceMap(data, placeHolders);
        return doReplace(replaceMap, template);
    }

    /**
     * 获取替换map
     * @param data
     * @param placeHolders
     * @return
     */
    private static Map<String, String> getReplaceMap(RequestData data, List<String> placeHolders) {
        Map<String, String> replaceMap = new HashMap<>(placeHolders.size());
        Map<String, Object> paramMap;
        for (String placeHolder : placeHolders) {
            String[] placeHolderArray = placeHolder.split(":");
            if (placeHolderArray.length == 1) {
                AbstractParser parser = PARSER_MAP.get(placeHolder.toUpperCase());
                if (null == parser) {
                    parser = DEFAULT_PARSER;
                    PARSER_MAP.put(placeHolder, parser);
                }
                String result = parser.parse(placeHolder, data);
                replaceMap.put(placeHolder, result);
            } else {
                String place = placeHolderArray[0].toUpperCase();
                if (QUERY.equalsIgnoreCase(place)) {
                    // query中取值
                    paramMap = data.getQuery();
                } else if (BODY.equalsIgnoreCase(place)) {
                    // body中取值
                    paramMap = data.getBody();
                } else {
                    throw new RuntimeException("unknown place holder");
                }
                replaceMap.put(placeHolder, processMultiplePlaceHolder(placeHolderArray, paramMap));
            }
        }
        return replaceMap;
    }

    /**
     * 获取复杂参数
     * 例如：body:inner1:inner2:inner3:data
     * 在以下 body 中获取的值为 "testData"
     * {
     *     "inner1": {
     *         "inner2": {
     *             "inner3": {
     *                 "data": "testData"
     *             }
     *         }
     *     }
     * }
     * @param placeHolderArray
     * @param paramMap
     * @return
     */
    private static String processMultiplePlaceHolder(final String[] placeHolderArray, final Map<String, Object> paramMap) {
        int length = placeHolderArray.length;
        Map<String, Object> map = paramMap;
        Object result = "";
        for (int i = 1; i < length; i++) {
            result = map.get(placeHolderArray[i]);
            if (i == length - 1) {
                break;
            }
            if (result instanceof Map) {
                map = (Map)result;
            }
        }
        if (log.isDebugEnabled() && null == result) {
            log.debug("result not found: {}", placeHolderArray.toString());
        }
        return result == null ? "" : result.toString();
    }

    /**
     * 处理占位符
     * @param template
     * @return
     */
    private static List<String> processPlaceHolder(String template) {
        char[] chars = template.toCharArray();
        List<Integer> posList = new ArrayList<>(16);
        // 处理占位符
        // 类似 {{body:helloWorld}} 会被解析成 body helloWorld
        // 用于从body中的helloWorld字段中取值并返回
        for (int i = 1; i < chars.length; i ++) {
            if (chars[i] == '{' && chars[i - 1] == '{') {
                posList.add(i);
            }
            if (chars[i] == '}' && chars[i - 1] == '}') {
                posList.add(i - 1);
            }
        }
        List<String> placeHolders = new ArrayList<>(8);
        for (int i = 1; i < posList.size(); i++) {
            if (i % 2 == 1) {
                placeHolders.add(template.substring(posList.get(i - 1) + 1, posList.get(i)));
            }
        }
        return placeHolders;
    }

    /**
     * 替换模板字符串
     * @param replaceMap
     * @param template
     * @return
     */
    private static final String doReplace(Map<String, String> replaceMap, String template) {
        String result = template;
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            StringBuilder placeHolder = new StringBuilder();
            placeHolder.append(PREFIX).append(PREFIX).append(entry.getKey()).append(SUFFIX).append(SUFFIX);
            StringBuilder data = new StringBuilder();
            data.append("\"").append(entry.getValue()).append("\"");
            result = result.replace(placeHolder.toString(), data.toString());
        }
        return result;
    }

    private static final String getString(String placeHolder, Object data) {
        return null == data ? placeHolder : data.toString();
    }

}
