package org.nashorn.server.core;

import org.apache.log4j.Logger;

import javax.script.CompiledScript;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionUnitPool {

    private static final Logger LOGGER = Logger.getLogger(ExecutionUnitPool.class);

    public static ExecutionUnitPool instance() {
        LOGGER.info("INSTANCE : " + Holder.INSTANCE);
        return Holder.INSTANCE;
    }

    private static class Holder {
        private Holder() {}
        private static final ExecutionUnitPool INSTANCE = new ExecutionUnitPool(poolSize);
    }

    private static final int DEFAULT_POOL_SIZE = 3;

    private static int poolSize = DEFAULT_POOL_SIZE;

    public static void setPoolSize(int nThreads) {
        poolSize = nThreads;
    }

    private final ExecutorService executor;

    private ExecutionUnitPool(int nThreads) {
        executor = Executors.newFixedThreadPool(nThreads);
    }

    public ScriptExecutionUnit evalAsync(CompiledScript script) {
        return new ScriptExecutionUnit(script, executor);
    }
}