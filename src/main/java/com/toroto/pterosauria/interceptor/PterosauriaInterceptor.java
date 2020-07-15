package com.toroto.pterosauria.interceptor;

import com.toroto.pterosauria.handler.AbstractHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
public class PterosauriaInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AbstractHandler.process(request, response);
        return false;
    }
}
