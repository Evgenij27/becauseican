package org.nashorn.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Href {

    private final String self;

    private Href(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public static class Builder {

        private StringBuilder url;

        public Builder(StringBuilder url) {
            this.url = url;
        }

        private boolean checkForTrailingSlash() {
            int tailIndex = url.length() - 1;
            return url.charAt(tailIndex) == '/';
        }

        public Builder append(long id) {
            if (!checkForTrailingSlash()) {
                url.append("/").append(id);
            } else {
                url.append(id);
            }
            return this;
        }

        public Href build() {
            return new Href(url.toString());
        }

    }

}
