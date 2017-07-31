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
        Without check yet
     */
    public static void init(PoolSettings settings) {
        CommonPool.corePoolSize    = settings.getCorePoolSize();
        CommonPool.maximumPoolSize = settings.getMaximumPoolSize();
        CommonPool.keepAliveTime   = settings.getKeepAliveTime();
        CommonPool.timeUnit        = resolveTimeUnit(settings.getTimeUnit());
        CommonPool.workQueueSize   = settings.getWorkQueueSize();
    }

    /*
        Better move this ability to Jackson JSON Parser.
        Resolve time unit during convertion if it's *возможно*.
        .
     */
    private static TimeUnit resolveTimeUnit(String tunit) {
        TimeUnit timeUnit = null;
        switch (tunit) {
            case "m"  : timeUnit = TimeUnit.MINUTES;      break;
            case "s"  : timeUnit = TimeUnit.SECONDS;      break;
            case "ms" : timeUnit = TimeUnit.MILLISECONDS; break;
            case "us" : timeUnit = TimeUnit.MICROSECONDS; break;
            case "ns" : timeUnit = TimeUnit.NANOSECONDS;  break;
            default   : throw new IllegalArgumentException();
        }
        return timeUnit;
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
