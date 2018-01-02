package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.core.ExecutionUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptResponseFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ScriptResponseFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static String newJsonResponseFor(ExecutionUnit unit, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String bresp = null;
        if (unit.finishedExceptionally()) {
            bresp = convertToJson(buildResponseWithError(unit, req, resp));
        } else {
            bresp = convertToJson(buildResponse(unit, req, resp));
        }
        return bresp;
    }

    private static ScriptResponse buildResponse(ExecutionUnit unit, HttpServletRequest req, HttpServletResponse resp) {
        return new ScriptResponse(HttpServletResponse.SC_OK, copyHeaders(resp), unit.getResultOutput(),
                unit.finishedExceptionally(), unit.isDone());
    }

    private static ScriptResponse buildResponseWithError(ExecutionUnit unit, HttpServletRequest req, HttpServletResponse resp) {
        return new ScriptResponseWithError(HttpServletResponse.SC_FORBIDDEN, copyHeaders(resp), unit.getResultOutput(),
                unit.finishedExceptionally(), unit.isDone(), new ErrorData(unit.getCause()));
    }

    private static String convertToJson(Object value) throws IOException {
        return MAPPER.writeValueAsString(value);
    }

    private static Map<String, String> copyHeaders(HttpServletResponse resp) {
        Map<String, String> headers = new HashMap<>();
        for (String name : resp.getHeaderNames()) {
            headers.put(name, resp.getHeader(name));
        }
        return headers;
    }
}
