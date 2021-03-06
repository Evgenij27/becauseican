package org.nashorn.server.core;

import javax.script.*;

public class NashornScriptCompiler {

    private static final String ENGINE_NAME = "nashorn";

    public CompiledScript compile(String script) throws ScriptException {
        Compilable compilable = castToCompilable(newEngine(ENGINE_NAME));
        return compilable.compile(script);
    }

    private ScriptEngine newEngine(String engineName) {
        return new ScriptEngineManager().getEngineByName(engineName);
    }

    private Compilable castToCompilable(ScriptEngine engine) {
        return (Compilable) engine;
    }

}
