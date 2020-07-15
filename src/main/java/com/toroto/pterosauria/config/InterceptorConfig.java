package com.toroto.pterosauria.config;

import com.toroto.pterosauria.interceptor.PterosauriaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PterosauriaInterceptor())
                .addPathPatterns("/**");
    }
}
