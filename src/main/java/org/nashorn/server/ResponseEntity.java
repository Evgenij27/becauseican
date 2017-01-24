package org.nashorn.server;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseEntity {

    private int statusCode;
    private final Map<String, String> headerMap = new HashMap<>();

    private OutputStream body;

    public void setHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }




}
