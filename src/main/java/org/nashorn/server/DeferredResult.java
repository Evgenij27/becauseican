package org.nashorn.server;

import java.io.Writer;

public interface DeferredResult {

    Writer getOutputWriter();

    Writer getErrorOutputWriter();

    boolean isDone();

    boolean cancel(boolean mayInterruptIfRanning);
}
