package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.util.Map;

public class RequestObject {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String uri;

    private String method;

    private Map<String, String> headers;

    private ScriptEntity body;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public ScriptEntity getBody() {
        return body;
    }

    public void setBody(ScriptEntity body) {
        this.body = body;
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
