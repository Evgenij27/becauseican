package org.nashorn.server.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    public static ScriptEntity readEntity(Reader reader) throws JsonParseException, JsonMappingException, IOException {
        return MAPPER.readValue(reader, ScriptEntity.class);
    }

    public static String writeEntity(Object obj) throws IOException {
        return MAPPER.writeValueAsString(obj);
    }
}
