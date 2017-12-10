package org.nashorn.server;

public class ErrorScriptResponse extends ScriptResponse  {

    private final String message;

    public ErrorScriptResponse(int code, String status, String data, String message) {
        super(code, status, data);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
