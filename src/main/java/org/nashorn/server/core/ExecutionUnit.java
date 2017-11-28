package org.nashorn.server.core;

import org.nashorn.server.Snapshot;

public interface ExecutionUnit {

    long getId();

    boolean isDone();

    void cancel(boolean mayInterruptIfRunning);

    Snapshot takeSnapshot();

}
