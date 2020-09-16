package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;
import org.springframework.stereotype.Component;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
@Component
public class DefaultParser extends AbstractParser {

    @Override
    public String parse(String placeHolder, RequestData data) {
        Object result = null == data.getBody().get(placeHolder) ? null : data.getBody().get(placeHolder);
        result = null == result ? data.getQuery().get(placeHolder) : result;
        return result.toString();
    }
}
