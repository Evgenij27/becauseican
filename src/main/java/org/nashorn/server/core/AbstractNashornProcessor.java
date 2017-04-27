package org.nashorn.server.core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Writer;

public abstract class AbstractNashornProcessor implements NashornProcessor {

    protected final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    protected final Writer resultWriter;

    protected final Writer errorWriter;

    protected AbstractNashornProcessor(AbstractNashornProcessorBuilder builder) {
        this.resultWriter = builder.resultWriter;
        this.errorWriter  = builder.errorWriter;
        this.engine.getContext().setWriter(this.resultWriter);
        this.engine.getContext().setErrorWriter(this.errorWriter);
    }

    @Override
    public abstract Result process() throws ScriptException;

}
