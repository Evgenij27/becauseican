package org.nashorn.server.core;

import org.apache.log4j.Logger;
import org.nashorn.server.Snapshot;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicLong;

public class ScriptExecutionUnit implements ExecutionUnit {

    private static final Logger LOGGER = Logger.getLogger(ScriptExecutionUnit.class);

    private static final AtomicLong COUNTER = new AtomicLong(1);

    private final long id;

    private final StringWriter resultWriter = new StringWriter();
    private final StringWriter errorWriter = new StringWriter();

    private volatile Future<?> future;

    public ScriptExecutionUnit(CompiledScript script, ExecutorService executor) {
        this.id  = COUNTER.getAndIncrement();
        this.future = evalAsync(script, executor);
    }

    private Future<?> evalAsync(CompiledScript script, ExecutorService executor) {
        return executor.submit(() -> {
            try {
                script.eval(buildContext());
            } catch (ScriptException ex) {
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

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public void cancel(boolean mayInterupIfRunning) {
        future.cancel(mayInterupIfRunning);
    }

    @Override
    public Snapshot takeSnapshot() {
        return Snapshot.newSnapshot(resultWriter.getBuffer());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScriptExecutionUnit that = (ScriptExecutionUnit) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ScheduledExecutionUnit{" +
                ", id=" + id +
                '}';
    }
}

