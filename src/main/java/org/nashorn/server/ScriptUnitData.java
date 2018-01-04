package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.nashorn.server.core.ExecutionUnit;

@JsonInclude(Include.NON_NULL)
public class ScriptUnitData {

    private final String data;
    private final boolean hasError;
    private final boolean isFinished;
    private final ErrorData error;

    private ScriptUnitData(Builder b) {
        if (b.unit.finishedExceptionally()) {
            this.data = b.unit.getErrorOutput();
            this.hasError = true;
            this.isFinished = true;
            this.error = new ErrorData(b.unit.getCause());
        } else {
            this.data = b.unit.getResultOutput();
            this.hasError = false;
            this.isFinished = b.unit.isDone();
            this.error = null;
        }
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

    public static class Builder {
        private ExecutionUnit unit;

        public Builder unit(ExecutionUnit unit) {
            this.unit = unit;
            return this;
        }

        public ScriptUnitData build() {
            return new ScriptUnitData(this);
        }
    }
}
