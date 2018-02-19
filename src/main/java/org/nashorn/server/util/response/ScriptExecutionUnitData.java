package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.nashorn.server.core.ExecutionUnit;

@JsonSerialize(using = ScriptExecutionUnitDataSerializer.class)
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

    public long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public ExecutionUnit getUnit() {
        return unit;
    }
}
