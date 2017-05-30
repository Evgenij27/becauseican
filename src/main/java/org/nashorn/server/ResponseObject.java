package org.nashorn.server;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ResponseObject {

    private final long id;

    public ResponseObject(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    private int statusCode = HttpServletResponse.SC_OK;

    private Map<String, String> headers;

    private String body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "id=" + id +
                ", statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
