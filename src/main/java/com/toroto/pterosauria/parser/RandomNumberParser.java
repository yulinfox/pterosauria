package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;
import com.toroto.pterosauria.parser.processor.ParseProcessor;
import org.springframework.stereotype.Component;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
@Component
public class RandomNumberParser extends AbstractParser {

    public RandomNumberParser() {
        ParseProcessor.register("RANDOM_NUMBER", this);
    }

    @Override
    public String parse(String placeHolder, RequestData data) {
        return String.format("%d", System.currentTimeMillis());
    }
}
