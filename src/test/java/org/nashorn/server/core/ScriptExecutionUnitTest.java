package org.nashorn.server.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import javax.script.*;

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

public class ScriptExecutionUnitTest {

    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    private CompiledScript script = mock(CompiledScript.class);

    @Test
    public void testExceptionDuringExecutionMarkerFieldWasSetted() throws ScriptException {
        when(script.eval(any(ScriptContext.class))).thenThrow(ScriptException.class);

        ScriptExecutionUnit unit = new ScriptExecutionUnit(script, executor);
        sleep();
        assertNotNull(unit.getCause());
        assertTrue(unit.finishedExceptionally());
    }

    @Test
    public void testResultAndErrorOutput() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);

        StringWriter error = new StringWriter();
        error.append("error");

        StringWriter result = new StringWriter();
        result.append("result");

        when(unit.getErrorOutput()).thenReturn(error);
        when(unit.getResultOutput()).thenReturn(result);


        assertNotNull(unit.getResultOutput());
        //assertEquals("result", unit.getResultOutput().getBuffer().toString());

        assertNotNull(unit.getErrorOutput());
        //assertEquals("error", unit.getErrorOutput().getBuffer().toString());
    }

    @Test
    public void testTaskCancellation() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);

        doNothing().when(unit).cancel(true);
        when(unit.isDone()).thenReturn(true);

        unit.cancel(true);
        assertTrue(unit.isDone());

        verify(unit).cancel(true);
        verify(unit).isDone();
    }


    private static void sleep() {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
