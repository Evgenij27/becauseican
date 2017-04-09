package org.nashorn.server;

import java.util.List;

public class ScriptEntity {

    private String script;
    private String mainFunctionName;
    private List<String> arguments;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getMainFunctionName() {
        return mainFunctionName;
    }

    public void setMainFunctionName(String mainFunctionName) {
        this.mainFunctionName = mainFunctionName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }


}
