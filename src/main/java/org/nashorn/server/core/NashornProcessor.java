package org.nashorn.server.core;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.*;

public class NashornProcessor {

    private final CompiledScript compiledScript;
    private Future<Void> future;

    public NashornProcessor(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    public NashornExecutionResult<Void> evalAsync(ExecutorService executor) throws ScriptException {
        synchronized (executor) {
            future = executor.submit(() -> {
                compiledScript.eval();
                return null;
            });
            return new SimpleNashornExecutionResult();
        }
    }

    /*
        I think it's terrible....
     */
    private class SimpleNashornExecutionResult implements NashornExecutionResult<Void> {

        private ScriptContext getEngineContext() {
            return NashornProcessor.this.compiledScript.getEngine().getContext();
        }

        @Override
        public StringBuffer getOutputBuffer() {
            Writer writer = getEngineContext().getWriter();
            StringWriter sw = (StringWriter) writer;
            return sw.getBuffer();
        }

        @Override
        public StringBuffer getErrorBuffer() {
            Writer writer = getEngineContext().getErrorWriter();
            StringWriter sw = (StringWriter) writer;
            return sw.getBuffer();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return future.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public boolean isDone() {
            return future.isDone();
        }

        @Override
        public Void get() throws InterruptedException, ExecutionException {
            return future.get();
        }

        @Override
        public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return future.get(timeout, unit);
        }
    }
}
