package org.nashorn.server.core;

import javax.script.*;

public class NashornCompilableEngineFactory {

    public Compilable newCompilableEngine(ScriptContext context) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Compilable compilable = null;
        if (engine == null) {
            compilable = null;
        } else {
            engine.setContext(context);
            compilable = (Compilable) engine;
        }
        return compilable;
    }
}
