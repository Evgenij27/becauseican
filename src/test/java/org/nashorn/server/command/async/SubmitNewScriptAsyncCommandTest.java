package org.nashorn.server.command.async;

import org.junit.Test;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.command.Command;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.ScriptExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SubmitNewScriptAsyncCommandTest {

    @Test
    public void submitNewScriptTest() throws IOException, CommandExecutionException, ServletException {
        String script = "{\"script\":\"print('Hello World!');\"}";

        StringBuffer sb = new StringBuffer("http://localhost:8080/nashorn/api/v0.9/async/script");

        PipedWriter pw = new PipedWriter();
        PipedReader pr = new PipedReader(pw);
        pw.write(script);
        BufferedReader br = new BufferedReader(pr);


        HttpServletRequest mreq = mock(HttpServletRequest.class);
        HttpServletResponse mresp = mock(HttpServletResponse.class);

        InMemoryStorage storage = mock(InMemoryStorage.class);
        ExecutionUnitPool pool = mock(ExecutionUnitPool.class);

        when(mreq.getRequestURL()).thenReturn(sb);
        when(mreq.getReader()).thenReturn(br);

        Command command = new SubmitNewScriptAsyncCommand();
        System.out.println("before execute");
        ScriptResponse sr = (ScriptResponse) command.execute(mreq, mresp);

        verify(mresp).setStatus(HttpServletResponse.SC_CREATED);

        assertEquals(sr.getStatus(), HttpServletResponse.SC_CREATED);
        assertNull(sr.getContent());
    }
}
