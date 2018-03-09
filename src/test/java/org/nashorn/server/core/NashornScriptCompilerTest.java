package org.nashorn.server.core;

import org.junit.Test;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NashornScriptCompilerTest {

    @Test
    public void testCompilation() throws ScriptException {

        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript script = compiler.compile("print('Hello!');");
        assertNotNull(script);

    }

    @Test(expected = ScriptException.class)
    public void testExceptionCase() throws ScriptException {

        NashornScriptCompiler compiler = mock(NashornScriptCompiler.class);

        when(compiler.compile(anyString())).thenThrow(new ScriptException("Compilation Exception"));
        compiler.compile(anyString());
    }


}
