package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SimpleNashornProcessor implements NashornProcessor {

    private final ScriptEngine engine =
            new ScriptEngineManager().getEngineByName("nashorn");;

    private CompiledScript compiledScript;

    private final StringWriter buffer;

    private final StringWriter error;

    public SimpleNashornProcessor() {
        this(new StringWriter(), new StringWriter());
    }

    public SimpleNashornProcessor(StringWriter buffer, StringWriter error) {
        this.buffer = buffer;
        this.error = error;
        engine.getContext().setWriter(this.buffer);
        engine.getContext().setErrorWriter(this.error);
    }

    public void setAndCheck(String script) throws ScriptException {
        Compilable compilable = (Compilable) engine;
        this.compiledScript = compilable.compile(script);
    }

    public void setAndCheck(Reader reader) throws ScriptException {
        Compilable compilable = (Compilable) engine;
        this.compiledScript = compilable.compile(reader);
    }

    @Override
    public void process() throws ScriptException {
        compiledScript.eval();
    }

    @Override
    public StringWriter getBuffer() {
        return buffer;
    }

    @Override
    public StringWriter getError() {
        return error;
    }
}
