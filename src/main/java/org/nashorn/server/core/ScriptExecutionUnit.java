package org.nashorn.server.core;

import org.apache.log4j.Logger;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScriptExecutionUnit implements ExecutionUnit {

    private static final Logger LOGGER = Logger.getLogger(ScriptExecutionUnit.class);

    private final StringWriter result = new StringWriter();
    private final StringWriter error = new StringWriter();


    private volatile  Future<?> future;
    private volatile Throwable throwable;

    private final AtomicBoolean finishedExceptionally = new AtomicBoolean();

    public ScriptExecutionUnit(CompiledScript script, ExecutorService exec) {
        future = exec.submit(() -> {
            try {
                LOGGER.info("START EVAL SCRIPT");
                script.eval(prepareContext());
                LOGGER.info("END EVAL SCRIPT");
            } catch (ScriptException ex) {
                synchronized (this) {
                    throwable = ex;
                    finishedExceptionally.set(true);
                }
            }
        });
    }

    private ScriptContext prepareContext() {
        ScriptContext context = new SimpleScriptContext();
        context.setErrorWriter(error);
        context.setWriter(result);
        return context;
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
        return throwable;
    }

    @Override
    public void cancel(boolean mayInterruptIfRunning) {
        future.cancel(mayInterruptIfRunning);
    }

    @Override
    public StringWriter getResultOutput() {
        return result;
    }

    @Override
    public StringWriter getErrorOutput() {
        return error;
    }
}
