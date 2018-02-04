package org.nashorn.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Reader;

public class JsonSerDesEngine {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    private JsonSerDesEngine() {}

    public static ScriptEntity readEntity(Reader reader) throws IOException {
        return MAPPER.readValue(reader, ScriptEntity.class);
    }

    public static String writeEntity(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
}
