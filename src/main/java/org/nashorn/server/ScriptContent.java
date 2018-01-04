package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.nashorn.server.core.ExecutionUnit;

@JsonInclude(Include.NON_NULL)
public class ScriptContent {

    private ScriptUnitData script;
    private Href href;

    private ScriptContent(ScriptUnitData script, Href href) {
        this.script = script;
        this.href = href;
    }

    public ScriptUnitData getScript() {
        return script;
    }

    public Href getHref() {
        return href;
    }

    public static class Builder {
        private ScriptUnitData script;
        private Href href;

        public Builder script(ExecutionUnit unit) {
            this.script = new ScriptUnitData.Builder().unit(unit).build();
            return this;
        }

        public Builder href(Href href) {
            this.href = href;
            return this;
        }

        public ScriptContent build() {
            return new ScriptContent(script, href);
        }
    }


}
