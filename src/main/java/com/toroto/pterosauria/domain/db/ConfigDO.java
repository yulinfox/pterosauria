package com.toroto.pterosauria.domain.db;

import com.toroto.pterosauria.utils.JsonUtil;
import lombok.*;
import org.hsqldb.lib.StringUtil;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mock_config")
public class ConfigDO {

    /**
     * id
     */
    private Long id;

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 请求http方法
     * GET | POST | PUT | DELETE
     */
    private String method;

    /**
     * 返回类型
     * @see {@link TypeEnum}
     */
    private Integer type;

    /**
     * 同步响应
     * 不为空
     */
    private String syncResponse;

    public String getSyncResponse() {
        if (StringUtil.isEmpty(syncResponse) && null != syncResponseObj) {
            syncResponse = JsonUtil.toJson(syncResponseObj);
        }
        return syncResponse;
    }

    /**
     * 同步返回实体类
     * 使用JsonUtil.toJson后值与字段syncResponse相同
     */
    @Transient
    private Object syncResponseObj;

    /**
     * 异步响应
     * type为1（异步）的时候不为空
     */
    private String asyncResponse;

    public String getAsyncResponse() {
        if (StringUtil.isEmpty(asyncResponse) && null != asyncResponseObj) {
            asyncResponse = JsonUtil.toJson(asyncResponseObj);
        }
        return asyncResponse;
    }

    /**
     * 异步返回实体类
     * 使用JsonUtil.toJson后值与字段asyncResponse相同
     */
    private Object asyncResponseObj;

    /**
     * 异步调用路径
     */
    private String asyncCallPath;

    /**
     * 异步接口请求方式
     */
    private String asyncMethod;

    /**
     * 异步接口contentType
     */
    private String asyncHttpHeader;

    public String getAsyncHttpHeader() {
        if (StringUtil.isEmpty(asyncHttpHeader) && null != asyncHttpHeaderMap) {
            asyncHttpHeader = JsonUtil.toJson(asyncHttpHeaderMap);
        }
        return asyncHttpHeader;
    }

    /**
     * 异步接口contentType
     */
    @Transient
    private Map<String, Object> asyncHttpHeaderMap;

    /**
     * 返回响应中的 contentType
     * 默认 application/json;charset=UTF-8
     */
    private String responseContentType;

    /**
     * 异步延迟时间，秒
     */
    private Integer delaySeconds;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum TypeEnum {

        SYNC(0, "同步"),
        ASYNC(1, "异步")
        ;

        @Getter
        private Integer code;

        @Getter
        private String desc;
    }
}
