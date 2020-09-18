package com.toroto.pterosauria.loader;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.manager.ConfigManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
public class DbConfigLoader extends AbstractConfigLoader {

    @Autowired
    private ConfigManager configManager;

    @Override
    public ConfigDO load(String uri, String method) {
        return configManager.getConfig(uri, method);
    }
}
