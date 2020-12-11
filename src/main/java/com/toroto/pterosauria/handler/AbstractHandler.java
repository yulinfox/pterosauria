package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.RequestData;
import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.loader.AbstractConfigLoader;
import com.toroto.pterosauria.manager.ConfigManager;
import com.toroto.pterosauria.utils.SpringFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
@Component
public abstract class AbstractHandler {

    private static AbstractConfigLoader CONFIG_LOADER;

    protected RequestData requestData;

    protected HttpServletRequest request;

    protected static Map<Integer, AbstractHandler> HANDLER_MAP = new ConcurrentHashMap<>(2);

    @Autowired
    public void setConfigLoader(AbstractConfigLoader configLoader) {
        CONFIG_LOADER = configLoader;
    }

    protected void parseRequest(HttpServletRequest request) throws Exception {
        this.request = request;
        requestData = new RequestData();
        requestData.parseBody(getBody());
        requestData.parseQuery(request.getQueryString());
        requestData.setMethod(request.getMethod());
        requestData.setUri(request.getRequestURI());
    }

    private String getBody() throws Exception {
        BufferedReader br = request.getReader();
        String str;
        StringBuilder body = new StringBuilder();
        while((str = br.readLine()) != null){
            body.append(str);
        }
        return body.toString();
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
        String uri = request.getRequestURI();
        String method = request.getMethod();
        ConfigDO config = CONFIG_LOADER.load(uri, method.toUpperCase());
        if (null == config) {
            config = CONFIG_LOADER.load(uri, method.toLowerCase());
            if (null == config) {
                log.warn("没有对应配置，请检查：uri = {}, method = {}", uri, method);
                return;
            }
        }
        AbstractHandler handler = HANDLER_MAP.get(config.getType());
        if (null == handler) {
            log.warn("配置存在问题，没有对应处理类：{}", config.getType());
            return;
        }
        handler.doReturn(request, response, config);
    }
}
