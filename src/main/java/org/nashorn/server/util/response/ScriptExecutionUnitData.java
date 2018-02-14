package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.nashorn.server.core.ExecutionUnit;

@JsonInclude(Include.NON_NULL)
public class ScriptExecutionUnitData {
    private long id;
    private String location;
    private ExecutionUnit unit;

    public void setId(long id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUnit(ExecutionUnit unit) {
        this.unit = unit;
    }
}
