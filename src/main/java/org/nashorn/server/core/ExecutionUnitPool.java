package org.nashorn.server.core;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
        private static final ExecutionUnitPool INSTANCE = new ExecutionUnitPool();
    }

    private static final int DEFAULT_POOL_SIZE = 3;

    private final ExecutorService executor;

    private ExecutionUnitPool() {
        int nThreads = DEFAULT_POOL_SIZE;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            nThreads = (Integer) envCtx.lookup("pool/size");
            LOGGER.info("Injected pool size is: " + nThreads);
        } catch (NamingException ex) {
            LOGGER.error("Property not found. Use default value", ex);

        }
        executor = Executors.newFixedThreadPool(nThreads);
    }

    public ScriptExecutionUnit evalAsync(CompiledScript script) {
        return new ScriptExecutionUnit(script, executor);
    }
}