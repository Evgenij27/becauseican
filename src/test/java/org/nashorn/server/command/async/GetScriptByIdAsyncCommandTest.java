package org.nashorn.server.command.async;

/*

public class GetScriptByIdAsyncCommandTest {

    private static InMemoryStorage db = InMemoryStorage.instance();

    private static ScriptExecutionUnit newUnit() {
        ExecutionUnitPool pool = ExecutionUnitPool.instance();
        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript script = null;
        try {
            script = compiler.compile("print('Hello!');");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return pool.evalAsync(script);
    }

    @Test
    public void testExecuteCommandSuccessfully() throws Exception {

        HttpRequestEntity reqEntity = mock(HttpRequestEntity.class);
        HttpResponseEntity respEntity = mock(HttpResponseEntity.class);

        long id = db.create(newUnit());

        when(reqEntity.supplyAsLong("id")).thenReturn(id);
        when(reqEntity.getRequestURL()).thenReturn(new StringBuffer("/foo/bar"));

        Command command = new GetScriptByIdAsyncCommand();

        ScriptResponse response = (ScriptResponse) command.execute(reqEntity, respEntity);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test(expected = CommandExecutionException.class)
    public void testExecuteCommandUnsuccessfullyPathVariableException() throws CommandExecutionException, ServletException {

        HttpRequestEntity reqEntity = mock(HttpRequestEntity.class);
        HttpResponseEntity respEntity = mock(HttpResponseEntity.class);

        try {
            when(reqEntity.supplyAsLong("id"))
                    .thenThrow(new PathVariableProcessingException("fail"));
            Command command = new GetScriptByIdAsyncCommand();
            command.execute(reqEntity, respEntity);
        } catch (PathVariableProcessingException ex) {
            fail();
        }
    }

    @Test(expected = CommandExecutionException.class)
    public void testExecuteCommandUnsuccessfullyUnitNotFound() throws CommandExecutionException, ServletException {

        HttpRequestEntity reqEntity = mock(HttpRequestEntity.class);
        HttpResponseEntity respEntity = mock(HttpResponseEntity.class);

        try {
            when(reqEntity.supplyAsLong("id")).thenReturn(1L);
            Command command = new GetScriptByIdAsyncCommand();
            command.execute(reqEntity, respEntity);
        } catch (PathVariableProcessingException ex) {
            fail();
        }
    }
}*/
