package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理同步请求
 * @author yulinfu
 * @date 2020/7/15
 */
@Component
public class SyncHandler extends AbstractHandler {

    public SyncHandler() {
        HANDLER_MAP.put(ConfigDO.TypeEnum.SYNC.getCode(), this);
    }

    @Override
    public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception {
        response.setContentType(config.getResponseContentType());
        response.getWriter().write(config.getSyncResponse());
    }
}
