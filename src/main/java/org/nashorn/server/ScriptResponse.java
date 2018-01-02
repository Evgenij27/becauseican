package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class ScriptResponse {

    private final int status;
    private final Map<String, String> headers;
    private final ScriptContext context;
    private final Href href;

    private ScriptResponse(Builder b) {
        this.status = b.status;
        this.headers = new HashMap<>(b.headers);
        this.context =
                new ScriptContext(b.data, b.hasError, b.isFinished, new ErrorData(b.t));
        this.href = new Href(b.href);
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ScriptContext getContext() {
        return context;
    }

    public Href getHref() {
        return href;
    }

    public class Builder {

        private int status;
        private Map<String, String> headers = new HashMap<>();

        private String data;
        private boolean hasError;
        private boolean isFinished;

        private Throwable t;

        private String href;

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder exceptionally(Throwable t) {
            this.hasError = true;
            this.t = t;
            return this;
        }

        public Builder isFinished(boolean isFinished) {
            this.isFinished = isFinished;
            return this;
        }

        public Builder setHrefSelf(String self) {
            this.href = self;
            return this;
        }

        public ScriptResponse build() {
            return new ScriptResponse(this);
        }

    }
}