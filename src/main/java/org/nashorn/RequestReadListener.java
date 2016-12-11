package org.nashorn;

import java.io.IOException;

import javax.servlet.ReadListener; 
import javax.servlet.ServletInputStream;

public class RequestReadListener implements ReadListener {

    private static final int BUFFER_SIZE = 4 * 1024;

    private byte[] buffer = new byte[BUFFER_SIZE];

    private final ServletInputStream input;
    private final StringBuilder destination;

    public RequestReadListener(ServletInputStream input, StringBuilder destination) {
        this.input = input;
        this.destination = destination;
    }

    @Override
    public void onAllDataRead() {
        try {
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    @Override
    public void onDataAvailable() {
        try {
            do {
                int length = input.read(buffer);
                destination.append(new String(buffer, 0, length));
            } while (input.isReady());
        } catch (IOException ex) {}
    }

    @Override
    public void onError(Throwable t) {}
}
