package org.nashorn.server.core;

import org.apache.log4j.Logger;

import javax.script.CompiledScript;
import java.util.concurrent.*;

public class ExecutionUnitPool {

    private static final Logger LOGGER = Logger.getLogger(ExecutionUnitPool.class);

    public static ExecutionUnitPool instance() {
        LOGGER.info("INSTANCE : " + Holder.INSTANCE);
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ExecutionUnitPool INSTANCE = new ExecutionUnitPool();
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    private ExecutionUnitPool() {}

    public ExecutionUnit evalAsync(CompiledScript script) {
        return new ScriptExecutionUnit(script, executor);
    }
}
