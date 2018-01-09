package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.servlet.http.HttpServletRequest;

@JsonInclude(Include.NON_NULL)
public class Href {

    private final String self;

    public Href(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public static HrefBuilder newBuilder(HttpServletRequest url) {
        return new HrefBuilder(url);
    }

}
