package org.nashorn.server;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;

public class InvalidValueException extends JsonProcessingException {

    public InvalidValueException(String msg, JsonLocation loc, Throwable rootCause) {
        super(msg, loc, rootCause);
    }

    public InvalidValueException(String msg) {
        super(msg);
    }

    public InvalidValueException(String msg, JsonLocation loc) {
        super(msg, loc);
    }

    public InvalidValueException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

    public InvalidValueException(Throwable rootCause) {
        super(rootCause);
    }
}
