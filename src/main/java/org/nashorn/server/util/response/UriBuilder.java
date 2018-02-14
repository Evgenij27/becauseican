package org.nashorn.server.util.response;

import org.nashorn.server.Builder;

import javax.servlet.http.HttpServletRequest;

public class UriBuilder implements Builder<String> {
    private final StringBuilder uri;

    public UriBuilder(HttpServletRequest req) {
        this.uri = new StringBuilder(req.getRequestURL());
    }

    private void appendTrailingSlashIfNeeded() {
        if (!checkForTrailingSlash()) {
            uri.append("/");
        }
    }

    private boolean checkForTrailingSlash() {
        return uri.charAt(uri.length() - 1) == '/';
    }

    public UriBuilder append(long id) {
        appendTrailingSlashIfNeeded();
        uri.append(id);
        return this;
    }

    @Override
    public String build() {
        return uri.toString();
    }


}
