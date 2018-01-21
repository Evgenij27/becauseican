package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.nashorn.server.core.ExecutionUnit;

@JsonInclude(Include.NON_NULL)
public class ScriptUnitData {

    @JsonProperty("data")
    private final String data;

    @JsonProperty("hasError")
    private final boolean hasError;

    @JsonProperty("isFinished")
    private final boolean isFinished;

    @JsonProperty("error")
    private final ErrorData error;

    public ScriptUnitData(ExecutionUnit unit) {
        if (unit.finishedExceptionally()) {
            this.data = unit.getErrorOutput().getBuffer().toString();
            this.hasError = true;
            this.isFinished = true;
            this.error = new ErrorData(unit.getCause());
        } else {
            this.data = unit.getResultOutput().getBuffer().toString();
            this.hasError = false;
            this.isFinished = unit.isDone();
            this.error = null;
        }
    }
}
