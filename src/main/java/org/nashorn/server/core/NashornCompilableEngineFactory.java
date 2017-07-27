package org.nashorn.server.core;

import javax.script.*;

public class NashornCompilableEngineFactory {

    private final ScriptContext context;

    public NashornCompilableEngineFactory() {
        this(new SimpleScriptContext());
    }

    public NashornCompilableEngineFactory(ScriptContext context) {
        this.context = context;
    }

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
