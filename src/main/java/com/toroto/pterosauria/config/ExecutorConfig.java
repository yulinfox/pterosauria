package com.toroto.pterosauria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(10, 15, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

}
