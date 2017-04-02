package org.nashorn.server;

import javax.script.ScriptEngine;
import java.io.Writer;
import java.util.concurrent.Future;

public class DeferredScriptResult implements DeferredResult {

    private final ScriptEngine engine;
    private final Future<?> future;

    private DeferredScriptResult(Builder builder) {
        this.engine = builder.engine;
        this.future = builder.future;
    }

    @Override
    public Writer getOutputWriter() {
        return this.engine.getContext().getWriter();
    }

    @Override
    public Writer getErrorOutputWriter() {
        return this.engine.getContext().getErrorWriter();
    }

    @Override
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }

    public static class Builder {

        private ScriptEngine engine;
        private Future<?> future;

        public Builder setEngine(ScriptEngine engine) {
            this.engine = engine;
            return this;
        }

        public Builder setFuture(Future<?> future) {
            this.future = future;
            return this;
        }

        public DeferredScriptResult build() {
            return new DeferredScriptResult(this);
        }
    }


}
