package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.util.response.ErrorData;

@JsonInclude(Include.NON_NULL)
public class ScriptUnitData {

    private final String data;
    private final boolean hasError;
    private final boolean isFinished;
    private final ErrorData error;

    public ScriptUnitData(ExecutionUnit unit) {
        if (unit.finishedExceptionally()) {
            this.data = unit.getErrorOutput();
            this.hasError = true;
            this.isFinished = true;
            this.error = new ErrorData(unit.getCause());
        } else {
            this.data = unit.getResultOutput();
            this.hasError = false;
            this.isFinished = unit.isDone();
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

}
