package org.nashorn.server;

import java.util.concurrent.*;

public class NashornTaskExecutor {

    private NashornTaskExecutor() {}

    private static ExecutorService service;

    public static ExecutorService getService(int corePoolSize,
                                             int maximumPoolSize,
                                             long keepAliveTime,
                                             TimeUnit timeUnit,
                                             BlockingQueue<Runnable> queue) {

        if (service == null) {
            service = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, queue);
        }
        return service;
    }

    public static ExecutorService getService() {
        return getService(10,
                20,
                0L,
                 TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<Runnable>(30));
    }



}