package org.nashorn.server;

import java.util.List;
import java.util.concurrent.*;

public class CommonPool extends ThreadPoolExecutor {

    /*
        Default settings for pool
     */
    private static int corePoolSize    = 10;
    private static int maximumPoolSize = 15;
    private static long keepAliveTime  = 0;
    private static TimeUnit timeUnit  = TimeUnit.MICROSECONDS;
    private static int workQueueSize   = 60;

    /*
        Value checked in deserializer
     */
    public static void init(PoolSettings settings) {
        CommonPool.corePoolSize    = settings.getCorePoolSize();
        CommonPool.maximumPoolSize = settings.getMaximumPoolSize();
        CommonPool.keepAliveTime   = settings.getKeepAliveTime();
        CommonPool.timeUnit        = settings.getTimeUnit();
        CommonPool.workQueueSize   = settings.getWorkQueueSize();
    }

    private static CommonPool instance;

    public static synchronized CommonPool getInstance() {
        if (instance == null) {
            instance = new CommonPool(
                    CommonPool.corePoolSize,
                    CommonPool.maximumPoolSize,
                    CommonPool.keepAliveTime,
                    CommonPool.timeUnit,
                    new ArrayBlockingQueue<Runnable>(CommonPool.workQueueSize));
        }
        return instance;
    }

    private CommonPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }




}
