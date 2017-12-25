package org.nashorn.server;

public class ScriptResponseWithError extends ScriptResponse {

    private final ErrorData error;

    protected ScriptResponseWithError(String status, String data, boolean hasError, boolean isFinished, ErrorData error) {
        super(status, data, hasError, isFinished);
        this.error = error;
    }

    public ErrorData getError() {
        return error;
    }
}
