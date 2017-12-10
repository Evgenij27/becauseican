package org.nashorn.server;

public class ScriptResponse {

    private final int code;
    private final String status;
    private final String data;

    public ScriptResponse(int code, String status, String data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
}