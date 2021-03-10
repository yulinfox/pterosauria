package com.toroto.pterosauria.handler;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.domain.delay.DelayedElement;
import com.toroto.pterosauria.parser.processor.ParseProcessor;
import com.toroto.pterosauria.task.AsyncCallTask;
import com.toroto.pterosauria.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * 处理异步请求
 * @author yulinfu
 * @date 2020/7/15
 */
@Slf4j
@Component
public class AsyncHandler extends AbstractHandler implements Runnable {

    private static final DelayQueue<DelayedElement> ASYNC_DELAY_QUEUE = new DelayQueue<>();

    private static final int THOUSAND = 1000;

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Method> methodMap = new HashMap<>(2);

    @Autowired
    private ExecutorService executor;

    public AsyncHandler() {
        HANDLER_MAP.put(ConfigDO.TypeEnum.ASYNC.getCode(), this);
        try {
            methodMap.put(HttpMethod.GET.name(), this.getClass().getMethod("doGetCall", ConfigDO.class));
            methodMap.put(HttpMethod.POST.name(), this.getClass().getMethod("doPostCall", ConfigDO.class));
        } catch (Exception e) {
            log.error("获取方法失败:{}", e);
            System.exit(1);
        }
        new Thread(this).start();
    }

    @Override
    public void doReturn(HttpServletRequest request, HttpServletResponse response, ConfigDO config) throws Exception {
        super.parseRequest(request);
        response.setContentType(config.getResponseContentType());
        String syncRes = ParseProcessor.parse(config.getSyncResponse(), this.requestData);
        response.getWriter().write(syncRes);
        log.info("同步响应已返回：{}", syncRes);
        doAsyncCall(config);
    }

    private void doAsyncCall(ConfigDO config) {
        Method method = methodMap.get(config.getAsyncMethod());
        if (null == method) {
            log.info("没有对应处理方法，请检查：{}", config.getAsyncMethod());
            return;
        }
        AsyncCallTask task = new AsyncCallTask(this, method, config);
        ASYNC_DELAY_QUEUE.offer(new DelayedElement(task, config.getDelaySeconds() * THOUSAND));
    }

    public void doPostCall(ConfigDO config) {
        HttpHeaders headers = getHttpHeaders(config);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity entity = new HttpEntity<>(ParseProcessor.parse(config.getAsyncResponse(), this.requestData), headers);
        Object response = restTemplate.postForObject(config.getAsyncCallPath(), entity, Object.class);
        log.info("POST: {}", response);
    }

    private HttpHeaders getHttpHeaders(ConfigDO config) {
        HttpHeaders headers = new HttpHeaders();
        if (!StringUtils.isEmpty(config.getAsyncHttpHeader())) {
            Map<String, String> headerMap = JsonUtil.fromJson(config.getAsyncHttpHeader(), Map.class);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }
        return headers;
    }

    public void doGetCall(ConfigDO config) {
        // create headers
        HttpHeaders headers = getHttpHeaders(config);
        HttpEntity request = new HttpEntity(headers);
        String requestParam = ParseProcessor.parse(config.getAsyncResponse(), this.requestData);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(config.getAsyncCallPath());
        if (!StringUtils.isEmpty(requestParam)) {
            Map<String, Object> paramMap = JsonUtil.fromJson(requestParam, Map.class);
            paramMap.entrySet()
                    .forEach(p -> uriBuilder.queryParam(p.getKey(), p.getValue()));
        }

        // make an HTTP GET request with headers
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.build().encode().toString(),
                HttpMethod.GET,
                request,
                String.class
        );
        log.info("GET: {}", response);
    }

    @Override
    public void run() {
        AsyncCallTask task;
        while (true) {
            try {
                task = (AsyncCallTask) ASYNC_DELAY_QUEUE.take().getObj();
                log.info("开始执行异步调用");
                executor.execute(task);
            } catch (InterruptedException e) {
                log.info("异步调用处理失败： ", e);
            }
        }
    }
}
