package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

public class PoolSettings {

    @JsonProperty
    private int corePoolSize;
    @JsonProperty
    private int maximumPoolSize;
    @JsonProperty
    private long keepAliveTime;
    @JsonProperty
    private String timeUnit;
    @JsonProperty
    private int workQueueSize;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }
}
