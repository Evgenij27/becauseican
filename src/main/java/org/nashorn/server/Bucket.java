package org.nashorn.server;

import javax.script.ScriptEngine;
import java.io.StringWriter;
import java.util.concurrent.Future;

public class Bucket {

    private final ScriptEngine engine;
    private final Runnable runnable;
    private final Future<?> future;

    private Bucket(Builder builder) {
        this.engine = builder.engine;
        this.runnable = builder.runnable;
        this.future = builder.future;
    }

    public boolean isDone() {
        return future.isDone();
    }

    public StringWriter getOutput() {
        return (StringWriter) engine.getContext().getWriter();
    }

    public StringWriter getError() {
        return (StringWriter) engine.getContext().getErrorWriter();
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public static class Builder {

        private ScriptEngine engine;
        private Runnable runnable;
        private Future<?> future;

        public Builder setEngine(ScriptEngine engine) {
            this.engine = engine;
            return this;
        }

        public Builder setRunnable(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        public Builder setFuture(Future<?> future) {
            this.future = future;
            return this;
        }

        public Bucket build() {
            return new Bucket(this);
        }
    }
}
