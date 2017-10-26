package org.nashorn.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestObject implements Serializable {

    private final long id;
    private final String uri;
    private final String method;
    private final Map<String, String> headers;
    private final String body;

    private RequestObject(Builder builder) {
        this.id      = builder.id;
        this.uri     = builder.uri;
        this.method  = builder.method;
        this.headers = new HashMap<>(builder.headers);
        this.body    = builder.body;
    }

    public long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {

        private long id;
        private String uri;
        private String method;
        private Map<String, String> headers = new HashMap<>();
        private String body;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
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

        public RequestObject build() {
            return new RequestObject(this);
        }
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
