package org.nashorn.server.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ScriptExecutionUnitSerializer extends StdSerializer<ScriptExecutionUnit> {

    public ScriptExecutionUnitSerializer() {this(null);}

    public ScriptExecutionUnitSerializer(Class<ScriptExecutionUnit> t) {
        super(t);
    }

    @Override
    public void serialize(ScriptExecutionUnit value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        if (value.finishedExceptionally()) {
            gen.writeStringField("data", value.getErrorOutput().getBuffer().toString());
            gen.writeBooleanField("hasError", true);
            gen.writeBooleanField("isFinished", true);

            gen.writeStartObject();

            gen.writeStringField("cause", value.getCause().getClass().getSimpleName());
            gen.writeStringField("message", value.getCause().getMessage());

            gen.writeEndObject();

        } else {
            gen.writeStringField("data", value.getResultOutput().getBuffer().toString());
            gen.writeBooleanField("hasError", false);
            gen.writeBooleanField("isFinished", value.isDone());
        }

        gen.writeEndObject();
    }
}
