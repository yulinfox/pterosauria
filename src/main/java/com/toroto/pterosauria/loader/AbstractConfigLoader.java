package com.toroto.pterosauria.loader;

import com.toroto.pterosauria.domain.db.ConfigDO;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
public abstract class AbstractConfigLoader {

    /**
     * 加载配置
     * @param uri
     * @param method
     * @return
     */
    public abstract ConfigDO load(String uri, String method);

}
