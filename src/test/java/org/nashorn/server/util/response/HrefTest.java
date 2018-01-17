package org.nashorn.server.util.response;

import org.junit.Test;
import static org.junit.Assert.*;
public class HrefTest {

    private static final String URL = "http://localhost/api/test/";

    @Test
    public void testHref() {
        StringBuilder sb = new StringBuilder(URL);
        Href.Builder hb = new Href.Builder(sb);
        hb.append(42L);
        Href href = hb.build();

        assertEquals(URL + 42L, href.getSelf());

    }
}
