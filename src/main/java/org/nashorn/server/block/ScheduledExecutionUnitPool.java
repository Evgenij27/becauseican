package org.nashorn.server.block;


import org.apache.log4j.Logger;
import org.nashorn.server.ScheduledTraverser;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ScheduledExecutionUnit;
import java.util.concurrent.*;

public class ScheduledExecutionUnitPool {

    private static final Logger LOGGER = Logger.getLogger(ScheduledExecutionUnitPool.class);

    private static final int UNITS_COUNT = 10;

    public static ScheduledExecutionUnitPool instance() {
        LOGGER.info("INSTANCE : " + Holder.INSTANCE);
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ScheduledExecutionUnitPool INSTANCE = new ScheduledExecutionUnitPool();
    }

    private final CopyOnWriteArraySet<ExecutionUnit> execUnits = new CopyOnWriteArraySet<ExecutionUnit>();
    private final ScheduledTraverser traverser = new ScheduledTraverser(0, 1000, TimeUnit.MILLISECONDS);

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    private ScheduledExecutionUnitPool() {
        fill();
        LOGGER.info(execUnits);
    }

    private void fill() {
        for (int i = 0; i < UNITS_COUNT; i++) {
            execUnits.add(new ScheduledExecutionUnit(executor));
        }
        traverser.registerForTraverse(execUnits);
    }

    public ExecutionUnit getUnit() {
        ExecutionUnit unit = null;
        for (ExecutionUnit execUnit : execUnits) {
            if (execUnit.isFree()) {
                execUnit.markAsBusy();
                unit = execUnit;
                break;
            }
        }
        LOGGER.info("Free Unit " + unit);
        return unit;
    }

    public void returnUnit(ExecutionUnit unit) {
        if (!unit.isFree()) {
            LOGGER.info("Return Unit " + unit);
            unit.markAsFree();
            unit.cleanUp();
        }
        LOGGER.info("After return : " + execUnits);
    }
}
