package org.nashorn.server;

import javax.script.ScriptContext;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class NashornWorker extends FutureTask<ScriptContext> {

    private final ScriptContext context;

    public NashornWorker(Callable<ScriptContext> callable, ScriptContext context) {
        super(callable);
        this.context = context;
    }

    public Writer getOutputWriter() {
        return this.context.getWriter();
    }

    public Writer getErrorWriter() {
        return this.context.getErrorWriter();
    }
}
