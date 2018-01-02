package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Href {

    private final String self;

    public Href(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }
}
