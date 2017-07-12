package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NashornService {

    private final long id;

    private final ExecutorService executor;

    private Writer outputBuffer;
    private Writer errorBuffer;

    private Future<NashornProcessor> personalFuture;

    public NashornService(long id, ExecutorService executor) {
        this.id           = id;
        this.executor     = executor;
    }

    public void setOutputBuffer(Writer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    public void setErrorBuffer(Writer errorBuffer) {
        this.errorBuffer = errorBuffer;
    }

    public Writer getOutputBuffer() {
        return outputBuffer;
    }

    public Writer getErrorBuffer() {
        return errorBuffer;
    }

    public void submit(String script) throws ScriptException {
         NashornProcessor processor = new StringNashornProcessor(script, outputBuffer, errorBuffer);
         personalFuture = executor.submit(new NashornWorker(processor));
    }

    public boolean isDone() {
        synchronized (this) {
            return personalFuture.isDone();
        }
    }

    public boolean isCancelled() {
        synchronized (this) {
            return personalFuture.isCancelled();
        }
    }

    public void cancel(boolean mayBeInterruptedIfRunning) {
        synchronized (this) {
            personalFuture.cancel(mayBeInterruptedIfRunning);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NashornService that = (NashornService) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
