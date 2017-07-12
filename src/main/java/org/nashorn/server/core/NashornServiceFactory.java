package org.nashorn.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class NashornServiceFactory {

    private final AtomicLong idFactory = new AtomicLong();

    private ExecutorService commonExecutor;

    private NashornServiceFactory() {}

    public static NashornServiceFactory newFactory() {
        return new NashornServiceFactory();
    }

    public void setCommonExecutor(ExecutorService commonExecutor) {
        this.commonExecutor = commonExecutor;
    }

    public NashornService newService() {
        return new NashornService(idFactory.getAndIncrement(), commonExecutor);
    }
}
