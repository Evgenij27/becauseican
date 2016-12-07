package org.nashorn;

import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class RequestReadListener implements ReadListener {

    private static final int BUFFER_SIZE = 4 * 1024;

    private byte[] buffer = new byte[BUFFER_SIZE];

    private final ServletInputStream input;
    private final StringBuilder data;

    public class RequestReadListener(ServletInputStream input, StringBuiler data) {
        this.input = input;
        this.data = data;
    }

    @Override
    public void onAllDataRead() {}

    @Override
    public void onDataAvailable() {
        try {
            do {
                int length = input.read(buffer);
                data.append(new String(buffer, 0, length))
            } while (input.isReady());
        } catch (IOException ex) {}
    }

    @Override
    public void onError(Throwable t) {}


}
