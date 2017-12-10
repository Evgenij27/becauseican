package org.nashorn.server.core;

public interface ExecutionUnit {

    boolean isDone();

    boolean finishedExceptionally();

    Throwable getCause();

    void cancel(boolean mayInterruptIfRunning);

    String getResultOutput();

    String getErrorOutput();

}
