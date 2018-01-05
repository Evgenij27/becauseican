package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ScriptContent {

    private ScriptUnitData script;
    private Href href;

    public ScriptUnitData getScript() {
        return script;
    }

    public void setScript(ScriptUnitData script) {
        this.script = script;
    }

    public Href getHref() {
        return href;
    }

    public void setHref(Href href) {
        this.href = href;
    }
}
