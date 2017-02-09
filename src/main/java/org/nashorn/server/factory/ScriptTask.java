package org.nashorn.server.factory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.*;

public class ScriptTask implements Runnable {

    private final ScriptEngine engine;
    private final FutureTask<?> futureTask;
    private final long id;

    ScriptTask(FutureTask<?> futureTask, ScriptEngine  engine, long id) {
        this.futureTask = futureTask;
        this.engine = engine;
        this.id = id;
    }

    public StringBuffer getOutput() {
        StringWriter writer = (StringWriter) engine.getContext().getWriter();
        return writer.getBuffer();
    }

    public StringBuffer getError() {
        StringWriter error = (StringWriter) engine.getContext().getErrorWriter();
        return error.getBuffer();
    }

    public boolean isDone() {
        return futureTask.isDone();
    }

    @Override
    public void run() {
        futureTask.run();
    }

    public boolean cancel() {
        return futureTask.cancel(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScriptTask task = (ScriptTask) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}