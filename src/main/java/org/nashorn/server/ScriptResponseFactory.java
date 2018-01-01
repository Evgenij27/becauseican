package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.core.ExecutionUnit;

import java.io.IOException;

public class ScriptResponseFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ScriptResponseFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static String newJsonResponseFor(ExecutionUnit unit) throws IOException {
        String resp = null;
        if (unit.finishedExceptionally()) {
            resp = convertToJson(buildResponseWithError(unit));
        } else {
            resp = convertToJson(buildResponse(unit));
        }
        return resp;
    }

    private static ScriptResponse buildResponse(ExecutionUnit unit) {
        return new ScriptResponse(StatusNames.SUCCESS.name(),
                unit.getResultOutput(), unit.finishedExceptionally(), unit.isDone());
    }

    private static ScriptResponse buildResponseWithError(ExecutionUnit unit) {
        return new ScriptResponseWithError(StatusNames.ERROR.name(),
                unit.getResultOutput(), unit.finishedExceptionally(), unit.isDone(), new ErrorData(unit.getCause()));
    }

    private static String convertToJson(Object value) throws IOException {
        return MAPPER.writeValueAsString(value);
    }
}
