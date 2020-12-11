package com.toroto.pterosauria.parser;

import com.toroto.pterosauria.domain.RequestData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuinfu
 * @date 2020/9/16
 */
public abstract class AbstractParser {

    private static final Map<String, AbstractParser> PARSER_MAP = new ConcurrentHashMap<>(4);

    /**
     * 默认处理类
     */
    private static AbstractParser DEFAULT_PARSER;

    /**
     * 分隔符
     */
    protected static final String SEP = "-";

    protected static final void register(String placeHolder, AbstractParser parser) {
        PARSER_MAP.put(placeHolder, parser);
    }

    protected static void setDefaultParser(DefaultParser defaultParser) {
        DEFAULT_PARSER = defaultParser;
    }

    /**
     * 替换
     * @param placeHolder
     * @param data
     * @return
     */
    abstract public String doParse(String placeHolder, RequestData data);

    public static final String parse(String placeHolder, RequestData data) {
        return getParser(placeHolder).doParse(placeHolder, data);
    }

    public static final AbstractParser getParser(String placeHolder) {
        String realPlaceHolder = getRealPlaceHolder(placeHolder);
        AbstractParser parser = PARSER_MAP.get(realPlaceHolder.toUpperCase());
        if (null == parser) {
            parser = DEFAULT_PARSER;
            PARSER_MAP.put(placeHolder, parser);
        }
        return parser;
    }

    public static String getRealPlaceHolder(String placeHolder) {
        return placeHolder.split(SEP)[0];
    }
}
