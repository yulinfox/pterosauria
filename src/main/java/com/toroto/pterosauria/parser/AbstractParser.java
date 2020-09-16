package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
public abstract class AbstractParser {

    abstract public String parse(String placeHolder, RequestData data);

}
