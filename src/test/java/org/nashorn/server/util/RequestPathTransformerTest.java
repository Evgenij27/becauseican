package org.nashorn.server.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestPathTransformerTest {

    private static final String TEMPLATE_REPLACEMENT = "(?<%s>[^/]+)";
    private static final String END_TEMPLATE = "(/)?";

    private static final String ROOT_PATH = "foo/";

    private static final String WITHOUT_VARIABLE_TEMPLATE = "hello/world/";
    private static final String WITH_VARIABLE_TEMPLATE = "hello/:world";

    @Test
    public void emptyRootPathWithoutVariableTest() {

        RequestPathTransformer rpt = new RequestPathTransformer("");
        String actual = rpt.transform(WITHOUT_VARIABLE_TEMPLATE);

        StringBuilder sb = new StringBuilder();
        sb.append("hello/world").append(END_TEMPLATE);

        assertEquals(sb.toString(), actual);
    }

    @Test
    public void emptyRootPathWithVariableTest() {

        RequestPathTransformer rpt = new RequestPathTransformer("");
        String actual = rpt.transform(WITH_VARIABLE_TEMPLATE);

        StringBuilder sb = new StringBuilder();
        sb.append("hello/").append("(?<world>[^/]+)").append(END_TEMPLATE);

        assertEquals(sb.toString(), actual);
    }

    @Test
    public void fullRootPathWithVariableTest() {

        RequestPathTransformer rpt = new RequestPathTransformer(ROOT_PATH);
        String actual = rpt.transform(WITH_VARIABLE_TEMPLATE);

        StringBuilder sb = new StringBuilder();
        sb.append(ROOT_PATH).append("hello/").append("(?<world>[^/]+)").append(END_TEMPLATE);

        assertEquals(sb.toString(), actual);
    }




}
