package org.nashorn.server.util.response;

public class HrefBuilder {

    private int originalBufferLen;
    private StringBuilder url;

    public HrefBuilder(StringBuilder url) {
        this.url = url;
        this.originalBufferLen = url.length();
    }

    private boolean checkForTrailingSlash() {
        int tailIndex = url.length() - 1;
        return url.charAt(tailIndex) == '/';
    }

    public HrefBuilder append(long id) {
        if (!checkForTrailingSlash()) {
            url.append("/").append(id);
        } else {
            url.append(id);
        }
        return this;
    }

    public Href build() {
        String self = url.toString();
        url.setLength(originalBufferLen);
        return new Href(self);
    }
}