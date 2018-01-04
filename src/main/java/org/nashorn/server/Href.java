package org.nashorn.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Href {

    private final String self;

    private Href(Builder b) {
        this.self = b.self;
    }

    public String getSelf() {
        return self;
    }

    public static class Builder {

        private String self;
        private int originalBufferLen;
        private StringBuffer baseUri;

        public Builder(StringBuffer baseUri) {
            this.baseUri = baseUri;
            this.originalBufferLen = baseUri.length();
        }

        private boolean checkForTrailingSlash() {
            int tailIndex = baseUri.length() - 1;
            return baseUri.charAt(tailIndex) == '/';
        }

        public Builder append(long id) {
            if (!checkForTrailingSlash()) {
                baseUri.append("/").append(id);
            } else {
                baseUri.append(id);
            }
            return this;
        }

        public Href build() {
            self = baseUri.toString();
            baseUri.setLength(originalBufferLen);
            return new Href(this);
        }
    }


}
