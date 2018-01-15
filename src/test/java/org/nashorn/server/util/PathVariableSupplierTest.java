package org.nashorn.server.util;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

public class PathVariableSupplierTest {

    @Test
    public void getIntVariableTest() throws PathVariableProcessingException {

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("int")).thenReturn(42);

        PathVariableSupplier pvs = new PathVariableSupplier(req);

        assertEquals(42, pvs.supplyAsInt("int"));
    }

    @Test
    public void getLongVariableTest() throws PathVariableProcessingException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("long")).thenReturn(42L);

        PathVariableSupplier pvs = new PathVariableSupplier(req);

        assertEquals(42L, pvs.supplyAsLong("long"));
    }

    @Test
    public void getStringVariableTest() throws PathVariableProcessingException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("name")).thenReturn("nashorn");

        PathVariableSupplier pvs = new PathVariableSupplier(req);

        assertEquals("nashorn", pvs.supplyAsString("name"));
    }

    @Test(expected = PathVariableProcessingException.class)
    public void nullStringVariableTest() throws PathVariableProcessingException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("null")).thenReturn(null);

        PathVariableSupplier pvs = new PathVariableSupplier(req);
        pvs.supplyAsString("null");
    }

    @Test(expected = PathVariableProcessingException.class)
    public void intNumberFormatExceptionTest() throws PathVariableProcessingException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("int")).thenReturn("int");

        PathVariableSupplier pvs = new PathVariableSupplier(req);
        pvs.supplyAsInt("int");
    }

    @Test(expected = PathVariableProcessingException.class)
    public void longNumberFormatExceptionTest() throws PathVariableProcessingException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute("long")).thenReturn("long");

        PathVariableSupplier pvs = new PathVariableSupplier(req);
        pvs.supplyAsLong("long");
    }
}
