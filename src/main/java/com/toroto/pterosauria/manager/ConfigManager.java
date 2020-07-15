package com.toroto.pterosauria.manager;

import com.toroto.pterosauria.dao.ConfigDao;
import com.toroto.pterosauria.domain.db.ConfigDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Component
public class ConfigManager {

    @Autowired
    private ConfigDao configDao;

    public ConfigDO getConfig(String uri, String method) {
        Condition condition = new Condition(ConfigDO.class);
        condition.and()
                .andEqualTo("uri", uri)
                .andEqualTo("method", method);
        return configDao.selectByCondition(condition)
                .stream()
                .findFirst()
                .orElse(null);
    }

}
