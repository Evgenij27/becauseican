package org.nashorn.server.command.async;

import org.junit.Test;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.Command;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class SubmitNewScriptAsyncCommandTest {

    @Test
    public void submitNewScriptTest() throws IOException, CommandExecutionException, ServletException {
        String script = "{\"script\":\"print('Hello World!');\"}";

        StringBuffer sb = new StringBuffer("http://localhost:8080/nashorn/api/v0.9/async/script");

        PipedWriter pw = new PipedWriter();
        PipedReader pr = new PipedReader(pw);
        pw.write(script);
        BufferedReader br = new BufferedReader(pr);


        HttpRequestEntity mreq = mock(HttpRequestEntity.class);
        HttpResponseEntity mresp = mock(HttpResponseEntity.class);

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
