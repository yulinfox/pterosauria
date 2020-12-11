package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
@Slf4j
@Component
public class RandomNumberParser extends AbstractParser {

    /**
     * 指定位数
     */
    private static AtomicInteger counter = new AtomicInteger(0);

    private static LocalDateTime now = LocalDateTime.now();

    /**
     * 最低位数
     */
    private static final int MIN_NUMBER = 14;

    /**
     * 年月日时分秒
     * 14位
     */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public RandomNumberParser() {
        register("RANDOM_NUMBER", this);
    }

    @Override
    public String doParse(String placeHolder, RequestData data) {
        String[] placeHolders = placeHolder.split(SEP);
        String time = LocalDateTime.now().format(TIME_FORMAT);
        if (placeHolders.length == 2) {
            try {
                if (now.isBefore(LocalDateTime.now())) {
                    counter.set(0);
                }
                int number = Integer.parseInt(placeHolders[1]);
                if (number <= MIN_NUMBER) {
                    // 如果位数小于14位，不加时间前缀
                    return String.format(String.format("%%0%dd", number), counter.incrementAndGet());
                }
                int diff = number - MIN_NUMBER;
                String format = getFormat(diff);
                return String.format(format, time, counter.incrementAndGet());
            } catch (Exception e) {
                log.error("数字转化失败：", e);
            }
        }
        return time;
    }

    public static final String getFormat(int diff) {
        return String.format("%%s%%0%dd", diff);
    }
}
