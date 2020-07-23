package com.toroto.pterosauria.domain;

import org.junit.Test;

public class RequestDataTest {

    @Test
    public void getResponse() {

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
        assert result.equals(requestData.getResponse(template));
    }
}