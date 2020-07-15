package com.toroto.pterosauria.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
public class SpringFactoryUtil {

    private static ApplicationContext context;

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clz) {
        return context.getBean(clz);
    }

}
