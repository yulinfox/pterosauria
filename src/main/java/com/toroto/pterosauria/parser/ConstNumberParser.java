package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;
import com.toroto.pterosauria.domain.delay.DelayedElement;
import com.toroto.pterosauria.parser.processor.ParseProcessor;
import com.toroto.pterosauria.utils.NumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
@Slf4j
@Component
public class ConstNumberParser extends AbstractParser implements Runnable {

    private static final Map<RequestData, String> MAP = new ConcurrentHashMap<>(8);

    private static DelayQueue<DelayedElement> EXPIRE_DELAY_QUEUE = new DelayQueue<>();

    private static int DELAY_TIME = 60 * 1000;

    public ConstNumberParser() {
        ParseProcessor.register("CONST_NUMBER", this);
        new Thread(this).start();
    }

    @Override
    public String parse(String placeHolder, RequestData data) {
        String result = MAP.get(data);
        if (null == result) {
            result = NumberGenerator.generate();
            MAP.put(data, result);
        }
        EXPIRE_DELAY_QUEUE.offer(new DelayedElement(data, DELAY_TIME));
        return result;
    }

    @Override
    public void run() {
        DelayedElement element;
        while (true) {
            try {
                element = EXPIRE_DELAY_QUEUE.take();
                log.info("开始执行过期操作: {}", element.getObj());
                MAP.remove(element.getObj());
            } catch (InterruptedException e) {
                log.error("expire error happened: ", e);
            }

        }
    }
}
