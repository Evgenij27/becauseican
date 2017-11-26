package org.nashorn.server;

import org.nashorn.server.block.ScheduledExecutionUnitPool;

public abstract class AbstractCommand implements Command {

    protected final ScheduledExecutionUnitPool scheduledPool;

    public AbstractCommand(ScheduledExecutionUnitPool scheduledPool) {
        this.scheduledPool = scheduledPool;
    }

}
