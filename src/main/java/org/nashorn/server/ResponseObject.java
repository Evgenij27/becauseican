package org.nashorn.server;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ResponseObject {

    private final long id;
    private final int status;
    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;

    private ResponseObject(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.statusCode = builder.statusCode;
        this.headers = new HashMap<>(builder.headers);
        this.body = builder.body;
    }

    public static class Builder {
        private long id;
        private int status = 0;
        private int statusCode = HttpServletResponse.SC_OK;
        private Map<String, String> headers = new HashMap<>();
        private String body;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder addHeader(String hName, String hValue) {
            this.headers.put(hName, hValue);
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public ResponseObject build() {
            return new ResponseObject(this);
        }
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "id=" + id +
                ", status=" + status +
                ", statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
