package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class ScriptResponse {

    private final int status;
    private final Map<String, String> headers;
    private final ResponseMessage message;
    private final List<ScriptContent> content;

    private ScriptResponse(Builder b) {
        this.status = b.status;
        this.headers = new HashMap<>(b.headers);
        this.message = b.message;
        if (b.content != null) {
            this.content = new ArrayList<>(b.content);
        } else {
            this.content = null;
        }
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<ScriptContent> getContent() {
        return content;
    }

    public static class Builder {

        private int status = HttpServletResponse.SC_OK;
        private Map<String, String> headers = new HashMap<>();
        private ResponseMessage message;
        private List<ScriptContent> content = new ArrayList<>();

        public Builder statusOK() {
            this.status = HttpServletResponse.SC_OK;
            return this;
        }

        public Builder statusError() {
            this.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            return this;
        }

        public Builder statusCreated() {
            this.status = HttpServletResponse.SC_CREATED;
            return this;
        }

        public Builder setHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder copyHeadersFrom(HttpServletResponse resp) {
            for (String name : resp.getHeaderNames()) {
                this.headers.put(name, resp.getHeader(name));
            }
            return this;
        }

        public Builder addContent(ScriptContent content) {
            this.content.add(content);
            return this;
        }

        public Builder noContent() {
            this.content = null;
            return this;
        }

        public Builder withMessage(String msg) {
            this.message = new ResponseMessage(msg);
            return this;
        }

        public ScriptResponse build() {
            return new ScriptResponse(this);
        }

    }
}