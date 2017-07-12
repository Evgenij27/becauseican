package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ScriptEntity {

    @JsonProperty
    private String script;

    public String getScript() {
        return script;
    }

    @Override
    public String toString() {
        return "ScriptEntity{" +
                "script='" + script + '\'' +
                '}';
    }
}
