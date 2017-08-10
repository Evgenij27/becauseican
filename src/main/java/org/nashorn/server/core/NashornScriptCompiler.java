package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;

public class NashornScriptCompiler {

    private final Compilable compilable;

    public NashornScriptCompiler(Compilable compilable) {
        this.compilable = compilable;
    }

    public NashornExecutionTask compile(String script) throws ScriptException {
        CompiledScript compiledScript = compilable.compile(script);
        return new NashornExecutionTask(compiledScript);
    }

    public NashornExecutionTask compile(Reader reader) throws ScriptException {
        CompiledScript compiledScript = compilable.compile(reader);
        return new NashornExecutionTask(compiledScript);
    }

}
