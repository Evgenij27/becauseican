package org.nashorn.server.command.async;

/*
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

        assertEquals(HttpServletResponse.SC_CREATED, sr.getStatus());
        assertNull(sr.getContent());
    }
}
*/
