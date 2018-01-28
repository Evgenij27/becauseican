package org.nashorn.server.core;

import org.junit.Test;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScriptExecutionUnitTest {

    @Test
    public void testExceptionDuringExecutionMarkerFieldWasSetted() throws ScriptException {
        CompiledScript script = mock(CompiledScript.class);
        when(script.eval(any(ScriptContext.class))).thenThrow(ScriptException.class);

        ScriptExecutionUnit unit = new ScriptExecutionUnit(script, Executors.newFixedThreadPool(1));
        sleep();
        assertNotNull(unit.getCause());
        assertTrue(unit.finishedExceptionally());

        assertNotNull(unit.getResultOutput());
        assertNotNull(unit.getErrorOutput());

        assertTrue(unit.isDone());
    }

    @Test
    public void testCancellation() throws ScriptException {
        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript script = compiler.compile("var i = 10; while(i>0) {print('Hello!');}");

        ScriptExecutionUnit unit = new ScriptExecutionUnit(script, Executors.newFixedThreadPool(1));
        assertFalse(unit.isDone());
        sleep();
        unit.cancel(true);
        assertTrue(unit.isDone());
    }

    @Test
    public void testExecution() throws ScriptException {
        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript script = compiler.compile("print('Hello JS!');");

        ScriptExecutionUnit unit = new ScriptExecutionUnit(script, Executors.newFixedThreadPool(1));
        sleep();
        assertTrue((!unit.finishedExceptionally() && unit.isDone()));
    }

    private static void sleep() {
        try {
            Thread.sleep(1000 );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
