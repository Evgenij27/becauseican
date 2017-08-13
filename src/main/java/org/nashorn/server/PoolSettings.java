package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.concurrent.TimeUnit;

@JsonDeserialize(using = PoolSettingsDeserializer.class)
public class PoolSettings {

    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final int workQueueSize;

    public PoolSettings(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit timeUnit, int workQueueSize) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueueSize = workQueueSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }

    @Override
    public String toString() {
        return "PoolSettings{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", timeUnit='" + timeUnit + '\'' +
                ", workQueueSize=" + workQueueSize +
                '}';
    }
}
