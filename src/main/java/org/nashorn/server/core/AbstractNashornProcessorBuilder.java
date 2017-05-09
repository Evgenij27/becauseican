package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class AbstractNashornProcessorBuilder {

    protected Writer resultWriter = new StringWriter();
    protected Writer errorWriter = new StringWriter();

    public AbstractNashornProcessorBuilder saveResultTo(Writer resultWriter) {
        this.resultWriter = resultWriter;
        return this;
    }

    public AbstractNashornProcessorBuilder saveErrorTo(Writer errorWriter) {
        this.errorWriter = errorWriter;
        return this;
    }

    public abstract NashornProcessor build() throws ScriptException;
}