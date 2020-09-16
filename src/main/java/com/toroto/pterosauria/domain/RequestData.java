package com.toroto.pterosauria.domain;

import com.toroto.pterosauria.utils.JsonUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author yuinfu
 * @date 2020/7/22
 */
@Data
public class RequestData {

    /**
     * 路径url参数
     */
    public Map<String, Object> query;

    /**
     * body中的参数
     */
    public Map<String, Object> body;

    /**
     * 方法
     */
    public String method;

    /**
     * uri
     */
    public String uri;

    public static final char PREFIX = '{';
    public static final char SUFFIX = '}';

    private static final String QUERY = "QUERY";
    private static final String BODY = "BODY";
    // 占位符
    private static final String RANDOM_UUID = "RANDOM_UUID";
    private static final String RANDOM_NUMBER = "RANDOM_NUMBER";

    public RequestData() {
        query = new HashMap<>(8);
        body = new HashMap<>(8);
    }

    public void parseQuery(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return;
        }
        String[] queryArray = queryString.split("&");
        for (String queryCoupe : queryArray) {
            String[] coupeArray = queryCoupe.split("=");
            if (coupeArray.length <= 0) {
                continue;
            }
            this.query.put(coupeArray[0], coupeArray[1]);
        }
    }

    public void parseBody(String bodyString) {
        if (StringUtils.isEmpty(bodyString)) {
            return;
        }
        body = JsonUtil.fromJson(bodyString, HashMap.class);
    }

    /**
     * 格式化返回数据
     * 支持自定义
     * @param template
     * @return
     */
    public String getResponse(final String template) {
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

        // 占位符取值替换
        Map<String, String> replaceMap = new HashMap<>(placeHolders.size());
        for (String placeHolder : placeHolders) {
            String[] placeHolderArray = placeHolder.split(":");
            if (placeHolderArray.length == 1) {
                if (RANDOM_UUID.equalsIgnoreCase(placeHolder)) {
                    // 随机字符串
                    replaceMap.put(placeHolder, UUID.randomUUID().toString());
                } else if (RANDOM_NUMBER.equalsIgnoreCase(placeHolder)) {
                    // 随机数字
                    replaceMap.put(placeHolder, String.format("%d", System.currentTimeMillis()));
                } else {
                    Object data = null == body.get(placeHolder) ? null : body.get(placeHolder);
                    data = null == data ? query.get(placeHolder) : data;
                    replaceMap.put(placeHolder, data.toString());
                }
            } else if (placeHolderArray.length == 2) {
                String place = placeHolderArray[0].toUpperCase();
                String holder = placeHolderArray[1];
                if (QUERY.equalsIgnoreCase(place)) {
                    // query中取值
                    replaceMap.put(placeHolder, getString(placeHolder, query.get(holder)));
                } else if (BODY.equalsIgnoreCase(place)) {
                    // body中取值
                    replaceMap.put(placeHolder, getString(placeHolder, body.get(holder).toString()));
                }
            }
        }
        return doReplace(replaceMap, template);
    }

    /**
     * 替换模板字符串
     * @param replaceMap
     * @param template
     * @return
     */
    private String doReplace(Map<String, String> replaceMap, String template) {
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

    private String getString(String placeHolder, Object data) {
        return null == data ? placeHolder : data.toString();
    }

}
