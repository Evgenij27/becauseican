package org.nashorn.server.util.response;

import javax.servlet.http.HttpServletRequest;

public class HrefBuilder {

    private int originalBufferLen;
    private StringBuffer baseUri;

    public HrefBuilder(HttpServletRequest req) {
        this.baseUri = req.getRequestURL();
        this.originalBufferLen = this.baseUri.length();
    }

    private boolean checkForTrailingSlash() {
        int tailIndex = baseUri.length() - 1;
        return baseUri.charAt(tailIndex) == '/';
    }

    public HrefBuilder append(long id) {
        if (!checkForTrailingSlash()) {
            baseUri.append("/").append(id);
        } else {
            baseUri.append(id);
        }
        return this;
    }

    public Href build() {
        String self = baseUri.toString();
        baseUri.setLength(originalBufferLen);
        return new Href(self);
    }
}
