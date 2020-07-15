package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.manager.ConfigManager;
import com.toroto.pterosauria.utils.SpringFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
public abstract class AbstractHandler {

    private static ConfigManager configManager;

    protected static Map<Integer, AbstractHandler> HANDLER_MAP = new ConcurrentHashMap<>(2);

    protected ConfigDO getConfig(String uri, String method) {
        return configManager.getConfig(uri, method);
    }

    /**
     * 处理返回逻辑
     * @param request
     * @param response
     * @param config
     * @throws Exception
     */
    abstract public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception;

    public static void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (null == configManager) {
            configManager = SpringFactoryUtil.getBean(ConfigManager.class);
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        ConfigDO config = configManager.getConfig(uri, method);
        if (null == config) {
            log.warn("没有对应配置，请检查：uri = {}, method = {}", uri, method);
            return;
        }
        AbstractHandler handler = HANDLER_MAP.get(config.getType());
        if (null == handler) {
            log.warn("配置存在问题，没有对应处理类：{}", config.getType());
            return;
        }
        handler.doReturn(request, response, config);
    }
}
