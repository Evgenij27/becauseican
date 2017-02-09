package org.nashorn.server;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;

public class ResponseEntity {

    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;

    private ResponseEntity(Builder builder) {
        this.statusCode = builder.statusCode;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private int statusCode = HttpServletResponse.SC_OK;
        private Map<String, String> headers = new HashMap<>();
        private String body = "";

        public Builder setHeader(String header, String value) {
            headers.put(header, value);
            return this;
        }

        public Builder setBody(Object bodyObect)
                throws JsonProcessingException, JsonMappingException {
            this.body = new ObjectMapper().writeValueAsString(bodyObect);
            return this;
        }

        public Builder setStatus(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ResponseEntity build() {
            return new ResponseEntity(this);
        }
    }
}
