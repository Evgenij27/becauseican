package org.nashorn.server.async.command;

import org.junit.Test;
import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static org.mockito.Mockito.*;

public class SubmitNewScriptAsyncCommandTest {

    @Test
    public void testSubmitNewScript() throws IOException, CommandExecutionException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);


    }

}
