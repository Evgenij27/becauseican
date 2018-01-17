package org.nashorn.server.util.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorDataTest {

    @Test
    public void testErrorDataConstructor() {
        ErrorData ed = new ErrorData(new Throwable("Cause : Message" ));
        assertEquals("Message", ed.getMessage());
        assertEquals("Cause", ed.getCause());
    }


}
