package org.nashorn.server.core;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.*;

public class NashornExecutionTask extends FutureTask<Void> {

    private final CompiledScript compiledScript;
    //private Future<Void> future;

    public NashornExecutionTask(CompiledScript compiledScript) {
        super(() -> {
            compiledScript.eval();
            return null;
        });
        this.compiledScript = compiledScript;
    }

    public StringBuffer getOutputBuffer() {
        ScriptContext context = getContext();
        return ((StringWriter) context.getWriter()).getBuffer();
    }

    public StringBuffer getErrorBuffer() {
        ScriptContext context = getContext();
        return ((StringWriter) context.getErrorWriter()).getBuffer();
    }

    private ScriptContext getContext() {
        return compiledScript.getEngine().getContext();
    }
}
