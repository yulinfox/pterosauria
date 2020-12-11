package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
@Component
public class UUIDParser extends AbstractParser {

    public UUIDParser() {
        register("UUID", this);
    }

    @Override
    public String doParse(String placeHolder, RequestData data) {
        return UUID.randomUUID().toString();
    }
}
