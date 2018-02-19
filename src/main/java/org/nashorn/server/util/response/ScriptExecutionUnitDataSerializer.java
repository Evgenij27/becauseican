package org.nashorn.server.util.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ScriptExecutionUnitDataSerializer extends StdSerializer<ScriptExecutionUnitData> {

    public ScriptExecutionUnitDataSerializer() {
        this(null);
    }

    public ScriptExecutionUnitDataSerializer(Class<ScriptExecutionUnitData> t) {
        super(t);
    }

    @Override
    public void serialize(ScriptExecutionUnitData value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("id", value.getId());

        gen.writeFieldName("script");

        gen.writeStartObject();

        if (value.getUnit().finishedExceptionally()) {
            gen.writeStringField("data", value.getUnit().getErrorOutput().getBuffer().toString());
            gen.writeBooleanField("hasError", true);
            gen.writeBooleanField("isFinished", true);

            gen.writeStartObject();

            gen.writeStringField("cause", value.getUnit().getCause().getClass().getSimpleName());
            gen.writeStringField("message", value.getUnit().getCause().getMessage());

            gen.writeEndObject();

        } else {
            gen.writeStringField("data", value.getUnit().getResultOutput().getBuffer().toString());
            gen.writeBooleanField("hasError", false);
            gen.writeBooleanField("isFinished", value.getUnit().isDone());
        }

        gen.writeEndObject();

        gen.writeStringField("href", value.getLocation());

        gen.writeEndObject();

    }
}
