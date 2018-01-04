package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class JsonSerDesEngine {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonSerDesEngine() {}

    public static void indentOutput(boolean indent) {
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, indent);
    }

    public static ScriptEntity readEntity(Reader reader) throws IOException {
        return MAPPER.readValue(reader, ScriptEntity.class);
    }

    public static String writeEntity(ScriptResponse resp) throws IOException {
        return MAPPER.writeValueAsString(resp);
    }
}
