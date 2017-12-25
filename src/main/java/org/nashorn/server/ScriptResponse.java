package org.nashorn.server;

import org.nashorn.server.core.ExecutionUnit;

public class ScriptResponse {

    private final String status;
    private final String data;
    private final boolean hasError;
    private final boolean isFinished;

    public ScriptResponse(String status, String data, boolean hasError, boolean isFinished) {
        this.status = status;
        this.data = data;
        this.hasError = hasError;
        this.isFinished = isFinished;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public boolean isHasError() {
        return hasError;
    }

    public boolean isFinished() {
        return isFinished;
    }
}