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

        assertNotNull(unit.getResultOutput());
        assertNotNull(unit.getErrorOutput());

        assertTrue(unit.isDone());
    }
    
    private static void sleep() {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
