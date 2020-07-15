package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.task.AsyncCallTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理异步请求
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
@Component
public class AsyncHandler extends AbstractHandler {

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Method> methodMap = new HashMap<>(2);

    private ExecutorService executor = new ThreadPoolExecutor(3, 10, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public AsyncHandler() {
        HANDLER_MAP.put(ConfigDO.TypeEnum.ASYNC.getCode(), this);
        try {
            methodMap.put(HttpMethod.GET.name(), this.getClass().getMethod("doGetCall", ConfigDO.class));
            methodMap.put(HttpMethod.POST.name(), this.getClass().getMethod("doPostCall", ConfigDO.class));
        } catch (Exception e) {
            log.error("获取方法失败:{}", e);
            System.exit(1);
        }
    }

    @Override
    public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception {
        response.setContentType(config.getResponseContentType());
        response.getWriter().write(config.getSyncResponse());
        log.info("同步响应已返回：{}", config.getSyncResponse());
        doAsyncCall(config);
    }

    private void doAsyncCall(ConfigDO config) {
        Method method = methodMap.get(config.getAsyncMethod());
        if (null == method) {
            log.info("没有对应处理方法，请检查：{}", config.getAsyncMethod());
            return;
        }
        AsyncCallTask task = new AsyncCallTask(this, method, config);
        executor.execute(task);
    }

    public void doPostCall(ConfigDO config) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, config.getAsyncContentType());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity entity = new HttpEntity<>(config.getAsyncResponse(), headers);
        Object response = restTemplate.postForObject(config.getAsyncCallPath(), entity, Object.class);
        log.info("POST: {}", response);
    }

    public void doGetCall(ConfigDO config) {
        Object response = restTemplate.getForObject(config.getAsyncCallPath(), Object.class);
        log.info("GET: {}", response);
    }
}
