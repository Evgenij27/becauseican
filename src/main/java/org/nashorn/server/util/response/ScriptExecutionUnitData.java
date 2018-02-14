package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.nashorn.server.core.ExecutionUnit;

@JsonInclude(Include.NON_NULL)
public class ScriptExecutionUnitData {
    @JsonProperty("id")
    private long id;
    @JsonProperty("href")
    private String location;
    @JsonProperty
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
