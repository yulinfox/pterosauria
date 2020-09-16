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
    private Map<String, Object> query;

    /**
     * body中的参数
     */
    private Map<String, Object> body;

    /**
     * 方法
     */
    private String method;

    /**
     * uri
     */
    private String uri;

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
}
