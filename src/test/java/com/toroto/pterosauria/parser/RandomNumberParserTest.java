package com.toroto.pterosauria.parser;

import org.junit.Test;

public class RandomNumberParserTest {

    @Test
    public void testGetFormat() {
        assert "%s%03d".equalsIgnoreCase(RandomNumberParser.getFormat(3));
        assert "2020120111520030011".equalsIgnoreCase(
                String.format(RandomNumberParser.getFormat(4), "202012011152003", 11));
        assert "0001".equalsIgnoreCase(String.format(String.format("%%0%dd", 4), 1));
    }

    @Test
    public void doParse() {
        RandomNumberParser parser = new RandomNumberParser();
        assert 18 == parser.doParse("RANDOM_NUMBER-18", null).length();
    }

}