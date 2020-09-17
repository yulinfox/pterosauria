package com.toroto.pterosauria.parser.processor;

import com.toroto.pterosauria.domain.RequestData;
import com.toroto.pterosauria.parser.DefaultParser;
import com.toroto.pterosauria.parser.RandomNumberParser;
import com.toroto.pterosauria.parser.UUIDParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseProcessorTest {

    @Test
    public void parse() {
        // init ParseProcessor
        RandomNumberParser parser = new RandomNumberParser();
        UUIDParser uuidParser = new UUIDParser();
        DefaultParser defaultParser = new DefaultParser();

        String body = "{\n" +
                "\t\"testText\": \"text\",\n" +
                "\t\"queryTest1\": \"bodyTest1\",\n" +
                "\t\"testObj\": {\n" +
                "\t\t\"objText\": \"otext\",\n" +
                "\t\t\"objInt\": 1\n" +
                "\t}\n" +
                "}";
        String query = "queryTest1=test1&queryTest2=test2";
        RequestData requestData = new RequestData();
        requestData.parseQuery(query);
        requestData.parseBody(body);

        String template = "{\n" +
                "\t\"queryTest1\": {{query:queryTest1}},\n" +
                "\t\"queryTest2\": {{queryTest2}},\n" +
                "\t\"testText\": {{testText}},\n" +
                "\t\"bodyQueryText\": {{body:queryTest1}},\n" +
                "\t\"testObj\": {{testObj}}\n" +
                "}";

        String result = "{\n" +
                "\t\"queryTest1\": \"test1\",\n" +
                "\t\"queryTest2\": \"test2\",\n" +
                "\t\"testText\": \"text\",\n" +
                "\t\"bodyQueryText\": \"bodyTest1\",\n" +
                "\t\"testObj\": \"{objText=otext, objInt=1}\"\n" +
                "}";
        assert result.equals(ParseProcessor.parse(template, requestData));

    }
}