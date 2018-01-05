package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorData {

    private final String cause;
    private final String message;

    public ErrorData(Throwable t) {
            String[] splittedMessage = t.getMessage().split(":");
            this.cause = splittedMessage[0].trim();
            this.message = splittedMessage[1].trim();

    }

    public String getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }
}
