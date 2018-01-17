package org.nashorn.server.util.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ErrorDataTest {

    @Test
    public void testCauseErrorData() {
        ErrorData ed = mock(ErrorData.class);

        when(ed.getCause()).thenReturn("Cause");
        verify(ed).getCause();
    }

    @Test
    public void testMessageErrorData() {
        ErrorData ed = mock(ErrorData.class);

        when(ed.getMessage()).thenReturn("Message");
        verify(ed).getMessage();
    }

    @Test
    public void testErrorDataConstructor() {
        ErrorData ed = new ErrorData(new Throwable("Throwable", new Throwable()));
        assertEquals("Throwable", ed.getMessage());
    }


}
