package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class StringNashornProcessor extends AbstractNashornProcessor {

    private final CompiledScript compiledScript;

    public StringNashornProcessor(String script, Writer outputWriter, Writer errorWriter)
            throws ScriptException {
        super(outputWriter, errorWriter);
        this.compiledScript = compilable.compile(script);
    }

    @Override
    public void process() throws ScriptException {
        compiledScript.eval();
    }
}
