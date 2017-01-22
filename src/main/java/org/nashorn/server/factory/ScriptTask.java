package org.nashorn.server.factory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.*;

public class ScriptTask<V> implements RunnableFuture<V> {

    private final RunnableFuture<V> futureTask;
    private final ScriptEngine engine;
    private final long id;

    ScriptTask(Callable<V> callable, ScriptEngine engine, long id) {
        System.out.println("CALLABLE FROM CONSTRUCTOR ");
        this.engine = engine;
        this.id = id;
        this.futureTask = new FutureTask<V>(callable);
    }

    public StringWriter getWriter() {
        return (StringWriter) engine.getContext().getWriter();
    }

    public StringWriter getErrorWriter() {
        return (StringWriter) engine.getContext().getErrorWriter();
    }

    public long getId() {
        return id;
    }

    @Override
    public void run() {
        futureTask.run();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return futureTask.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return futureTask.isCancelled();
    }

    @Override
    public boolean isDone() {
        return futureTask.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return futureTask.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return futureTask.get(timeout, unit);
    }
}