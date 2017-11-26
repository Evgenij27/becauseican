package org.nashorn.server.core;

import org.apache.log4j.Logger;
import org.nashorn.server.Snapshot;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class ScheduledExecutionUnit implements ExecutionUnit {

    private static final Logger LOGGER = Logger.getLogger(ScheduledExecutionUnit.class);

    private static final AtomicLong COUNTER = new AtomicLong();
    private final ExecutorService executor;
    private final long id;

    private final AtomicBoolean free = new AtomicBoolean(true);

    private final StringWriter resultWriter = new StringWriter();
    private final StringWriter errorWriter = new StringWriter();

    private final BlockingQueue<Snapshot> transferQueue = new ArrayBlockingQueue<>(1);
    private volatile Future<?> future;


    public ScheduledExecutionUnit(ExecutorService executor) {
        this.id  = COUNTER.getAndIncrement();
        this.executor = executor;
    }

    @Override
    public void evalAsync(CompiledScript script) {
        this.future = executor.submit(() -> {
            try {
                script.eval(buildContext());
            } catch (ScriptException ex) {
                errorWriter.append(ex.getMessage());
            }
        });
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
        LOGGER.info("onTakeSnapshot");
        Snapshot snapshot = null;
        try {
            snapshot = transferQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return snapshot;
    }

    @Override
    public boolean isFree() {
        return free.get();
    }

    @Override
    public void markAsFree() {
        free.set(true);
    }

    @Override
    public void markAsBusy() {
        free.set(false);
    }

    @Override
    public void cleanUp() {
        clearBuffers();
        clearFuture();
        clearTransferQueue();
    }

    private void clearBuffers() {
        this.resultWriter.getBuffer().setLength(0);
        this.errorWriter.getBuffer().setLength(0);
    }

    private void clearFuture() {
        this.future = null;
    }

    private void clearTransferQueue() {
        this.transferQueue.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduledExecutionUnit that = (ScheduledExecutionUnit) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    private ScriptContext buildContext() {
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(resultWriter);
        context.setErrorWriter(errorWriter);
        return context;
    }

    @Override
    public void traverse() {
       if (!isFree()) {
           LOGGER.info("onTraverse : " + this);
           try {
               transferQueue.put(Snapshot.newSnapshot(resultWriter.getBuffer()));
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    public String toString() {
        return "ScheduledExecutionUnit{" +
                "free=" + free +
                ", id=" + id +
                '}';
    }
}
