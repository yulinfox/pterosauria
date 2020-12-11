package com.toroto.pterosauria.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractParserTest {

    @Test
    public void getRealPlaceHolder() {
        assert "TEST".equalsIgnoreCase(AbstractParser.getRealPlaceHolder("TEST"));
        assert "TEST1".equalsIgnoreCase(AbstractParser.getRealPlaceHolder("TEST1-ttt"));
    }
}