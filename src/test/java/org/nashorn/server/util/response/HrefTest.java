package org.nashorn.server.util.response;

import org.junit.Test;
import static org.junit.Assert.*;
public class HrefTest {

    private static final String URL_WITH_TAIL = "http://localhost/api/test/";
    private static final String URL_WITHOUT_TAIL = "http://localhost/api/test";

    @Test
    public void testHrefWithTail() {
        StringBuilder sb = new StringBuilder(URL_WITH_TAIL);
        Href.Builder hb = new Href.Builder(sb);
        hb.append(42L);
        Href href = hb.build();

        assertEquals(URL_WITH_TAIL + 42L, href.getSelf());
    }

    @Test
    public void testHrefWithoutTail() {
        StringBuilder sb = new StringBuilder(URL_WITHOUT_TAIL);
        Href.Builder hb = new Href.Builder(sb);
        hb.append(42L);
        Href href = hb.build();

        assertEquals(URL_WITHOUT_TAIL + '/' + 42L, href.getSelf());
    }
}
