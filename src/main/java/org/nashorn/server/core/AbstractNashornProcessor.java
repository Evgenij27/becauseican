package org.nashorn.server.core;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class AbstractNashornProcessor implements NashornProcessor {

    protected final Compilable compilable;

    public AbstractNashornProcessor(Writer outputWriter, Writer errorWriter) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.getContext().setWriter(outputWriter);
        engine.getContext().setErrorWriter(errorWriter);
        this.compilable = (Compilable) engine;
    }

}
