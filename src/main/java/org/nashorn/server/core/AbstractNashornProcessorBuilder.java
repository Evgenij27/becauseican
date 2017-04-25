package org.nashorn.server.core;

import java.io.StringWriter;
import java.io.Writer;

public abstract class AbstractNashornProcessorBuilder {

    protected Writer resultWriter = new StringWriter();
    protected Writer errorWriter = new StringWriter();

    public AbstractNashornProcessorBuilder setResultWriter(Writer resultWriter) {
        this.resultWriter = resultWriter;
        return this;
    }

    public AbstractNashornProcessorBuilder setErrorWriter(Writer errorWriter) {
        this.errorWriter = errorWriter;
        return this;
    }

    public abstract NashornProcessor build();
}