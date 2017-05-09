package org.nashorn.server.core;

import javax.script.*;
import java.io.Writer;

public abstract class AbstractNashornProcessor implements NashornProcessor {

    protected final Compilable compilable;

    protected final Writer resultWriter;

    protected final Writer errorWriter;

    protected AbstractNashornProcessor(AbstractNashornProcessorBuilder builder)  {
        this.resultWriter = builder.resultWriter;
        this.errorWriter  = builder.errorWriter;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.getContext().setWriter(this.resultWriter);
        engine.getContext().setErrorWriter(this.errorWriter);
        this.compilable = (Compilable) engine;
    }

    @Override
    public abstract Result process() throws ScriptException;

}
