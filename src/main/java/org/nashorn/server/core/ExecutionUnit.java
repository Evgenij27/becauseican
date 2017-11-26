package org.nashorn.server.core;

import org.nashorn.server.Snapshot;
import org.nashorn.server.Traversable;

import javax.script.CompiledScript;

public interface ExecutionUnit extends Traversable {

    void evalAsync(CompiledScript script);

    boolean isDone();

    void cancel(boolean mayInterruptIfRunning);

    Snapshot takeSnapshot();

    boolean isFree();

    void markAsFree();

    void markAsBusy();

    void cleanUp();


}
