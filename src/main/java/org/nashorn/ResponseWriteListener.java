package org.nashorn;

import java.io.IOException;

import javax.servlet.WriteListener;
import javax.servlet.ServletOutputStream;

public class ResponseWriteListener implements WriteListener {

    private final ServletOutputStream output;

    private final StringBuilder source;

    public ResponseWriteListener(ServletOutputStream output, StringBuilder source) {
        this.output = output;
        this.source = source;
    }

    @Override
    public void onError(Throwable t) {}

    @Override
    public void onWritePossible() throws IOException {
        int ptr = 0;
        while (output.isReady() && ptr < source.length()) {
            output.print(source.charAt(ptr++));
        }
    }
}
