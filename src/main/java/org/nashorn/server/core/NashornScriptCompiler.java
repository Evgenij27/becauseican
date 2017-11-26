package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;

public class NashornScriptCompiler {

    private static final String ENGINE_NAME = "nashorn";

    public CompiledScript compile(String script) throws ScriptException {
        Compilable compilable = castToCompilable(newEngine(ENGINE_NAME));
        return compilable.compile(script);
    }

    public CompiledScript compile(Reader reader) throws ScriptException {
        Compilable compilable = castToCompilable(newEngine(ENGINE_NAME));
        return compilable.compile(reader);
    }

    private ScriptEngine newEngine(String engineName) {
        return new ScriptEngineManager().getEngineByName(engineName);
    }

    private Compilable castToCompilable(ScriptEngine engine) {
        return (Compilable) engine;
    }

}
