package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理异步请求
 * @author yulinfu
 * @date 2020/7/15
 */
@Component
public class AsyncHandler extends AbstractHandler {

    public AsyncHandler() {
        HANDLER_MAP.put(ConfigDO.TypeEnum.ASYNC.getCode(), this);
    }

    @Override
    public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception {
        response.setContentType(config.getResponseContentType());
        response.getWriter().write(config.getSyncResponse());
    }
}
