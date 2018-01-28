package org.nashorn.server.core;

import org.junit.Test;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NashornScriptCompilerTest {

    private NashornScriptCompiler compiler = mock(NashornScriptCompiler.class);

    @Test
    public void testCompilation() throws ScriptException {

        when(compiler.compile(isA(String.class))).thenReturn(isA(CompiledScript.class));
        assertEquals(isA(CompiledScript.class), compiler.compile(isA(String.class)));
    }

    @Test(expected = ScriptException.class)
    public void testExceptionCase() throws ScriptException {
        when(compiler.compile(isA(String.class))).thenThrow(isA(ScriptException.class));
        compiler.compile(isA(String.class));
    }


}
