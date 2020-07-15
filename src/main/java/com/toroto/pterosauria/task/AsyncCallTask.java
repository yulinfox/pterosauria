package com.toroto.pterosauria.task;

import com.toroto.pterosauria.domain.db.ConfigDO;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
public class AsyncCallTask implements Runnable {

    private static final int THOUSAND = 1000;

    private ConfigDO config;

    private Method method;

    private Object object;

    public AsyncCallTask(Object object, Method method, ConfigDO config) {
        this.object = object;
        this.method = method;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            log.info("进入异步处理线程");
            Thread.sleep(config.getDelaySeconds() * THOUSAND);
            log.info("开始处理异步调用");
            method.invoke(object, config);
        } catch (Exception e) {
            log.info("请求出错：{}", e);
        }
    }
}
