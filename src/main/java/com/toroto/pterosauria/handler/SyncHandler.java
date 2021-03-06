package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.parser.processor.ParseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理同步请求
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
@Component
public class SyncHandler extends AbstractHandler {

    public SyncHandler() {
        HANDLER_MAP.put(ConfigDO.TypeEnum.SYNC.getCode(), this);
    }

    @Override
    public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception {
        super.parseRequest(request);
        response.setContentType(config.getResponseContentType());
        String res = ParseProcessor.parse(config.getSyncResponse(), this.requestData);
        response.getWriter().write(res);
        log.info("同步响应已返回：{}", res);
    }
}
