package org.nashorn.server.core;

import java.io.StringWriter;

public interface ExecutionUnit {

    boolean isDone();

    boolean finishedExceptionally();

    Throwable getCause();

    void cancel(boolean mayInterruptIfRunning);

    StringWriter getResultOutput();

    StringWriter getErrorOutput();

}
