package org.nashorn.server;

public class ErrorData {

    private final String cause;
    private final String message;

    public ErrorData() {
        this(null);
    }

    public ErrorData(Throwable t) {
        if (t != null) {
            String[] splittedMessage = t.getMessage().split(":");
            this.cause = splittedMessage[0].trim();
            this.message = splittedMessage[1].trim();
        } else {
            this.cause = "";
            this.message = "";
        }
    }

    public String getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }
}
