package org.nashorn.server.core;

import org.apache.log4j.Logger;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScriptExecutionUnit implements ExecutionUnit {

    private static final Logger LOGGER = Logger.getLogger(ScriptExecutionUnit.class);

    private final StringWriter resultWriter = new StringWriter();
    private final StringWriter errorWriter = new StringWriter();

    private final AtomicBoolean finishedExceptionally = new AtomicBoolean();

    private volatile Throwable cause;

    private volatile Future<?> future;

    public ScriptExecutionUnit(CompiledScript script, ExecutorService executor) {
        this.future = evalAsync(script, executor);
    }

    private Future<?> evalAsync(CompiledScript script, ExecutorService executor) {
        return executor.submit(() -> {
            try {
                LOGGER.info("EVAL SCRIPT");
                script.eval(buildContext());
            } catch (ScriptException ex) {
                processException(ex);
                LOGGER.error(ex);
            }
        });
    }

    private ScriptContext buildContext() {
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(resultWriter);
        context.setErrorWriter(errorWriter);
        return context;
    }

    private void processException(Exception ex) {
        synchronized (this) {
            errorWriter.append(ex.getMessage());
            finishedExceptionally.set(true);
            cause = ex;
        }
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public boolean finishedExceptionally() {
        return finishedExceptionally.get();
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public void cancel(boolean mayInterupIfRunning) {
        future.cancel(mayInterupIfRunning);
    }

    @Override
    public String getResultOutput() {
        return resultWriter.getBuffer().toString();
    }

    @Override
    public String getErrorOutput() {
        return errorWriter.getBuffer().toString();
    }
}

