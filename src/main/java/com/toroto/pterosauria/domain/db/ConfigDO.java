package com.toroto.pterosauria.domain.db;

import lombok.*;

import javax.persistence.Table;

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

    /**
     * 异步响应
     * type为1（异步）的时候不为空
     */
    private String asyncResponse;

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
    private String asyncContentType;

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
