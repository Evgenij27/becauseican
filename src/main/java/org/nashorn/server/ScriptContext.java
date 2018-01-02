package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ScriptContext {

    private final String data;
    private final boolean hasError;
    private final boolean isFinished;
    private final ErrorData error;

    public ScriptContext(String data, boolean hasError, boolean isFinished, ErrorData error) {
        this.data = data;
        this.hasError = hasError;
        this.isFinished = isFinished;
        this.error = error;
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

    public ErrorData getError() {
        return error;
    }
}
