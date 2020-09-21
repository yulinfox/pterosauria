package com.toroto.pterosauria.config;

import com.toroto.pterosauria.loader.AbstractConfigLoader;
import com.toroto.pterosauria.loader.DbConfigLoader;
import com.toroto.pterosauria.loader.FileConfigLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
@Configuration
public class LoaderConfig {

    @ConditionalOnExpression("#{'db'.equals(environment['config.loader'])}")
    @Bean
    @Primary
    public AbstractConfigLoader dbConfigLoader() {
        return new DbConfigLoader();
    }

    @Bean
    public AbstractConfigLoader fileConfigLoader() {
        return new FileConfigLoader();
    }

}
