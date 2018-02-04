package org.nashorn.server.resolver;

import org.junit.Test;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.Command;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.util.RequestPathTransformer;

import javax.servlet.ServletException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandResolverImplTest {


    private final RequestPathTransformer transformer = new RequestPathTransformer();

    @Test
    public void testCommandResolver() throws CommandNotFoundException {

        String path = "/foo";
        ConcurrentMap<String, Command> endpoints = new ConcurrentSkipListMap<>();

        HttpRequestEntity req = mock(HttpRequestEntity.class);
        when(req.getMethod()).thenReturn("GET");
        when(req.getRequestURI()).thenReturn(path);

        Command command = new TestCommand();

        endpoints.put(transformer.transform(path), command);

        CommandResolverImpl resolver = new CommandResolverImpl(endpoints, "GET", null);

        assertSame(command, resolver.resolve(req));
    }

    @Test(expected = CommandNotFoundException.class)
    public void testCommandNotFoundWrongURI() throws CommandNotFoundException {

        String path = "/foo/bar";

        ConcurrentMap<String, Command> endpoints = new ConcurrentSkipListMap<>();

        HttpRequestEntity req = mock(HttpRequestEntity.class);
        when(req.getMethod()).thenReturn("GET");
        when(req.getRequestURI()).thenReturn("/bar/foo");

        Command command = new TestCommand();

        endpoints.put(transformer.transform(path), command);

        CommandResolverImpl resolver = new CommandResolverImpl(endpoints, "GET", null);
        resolver.resolve(req);
    }

    @Test(expected = CommandNotFoundException.class)
    public void testCommandNotFoundWrongMethod() throws CommandNotFoundException {

        String path = "/foo/bar";

        ConcurrentMap<String, Command> endpoints = new ConcurrentSkipListMap<>();

        HttpRequestEntity req = mock(HttpRequestEntity.class);
        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn(path);

        Command command = new TestCommand();

        endpoints.put(transformer.transform(path), command);

        CommandResolverImpl resolver = new CommandResolverImpl(endpoints, "GET", null);
        resolver.resolve(req);
    }

    @Test
    public void testCommandChain() throws CommandNotFoundException {

        String path = "/foo/bar";

        ConcurrentMap<String, Command> getEndpoints = new ConcurrentSkipListMap<>();
        ConcurrentMap<String, Command> postEndpoints = new ConcurrentSkipListMap<>();
        Command getCommand = new TestCommand();
        Command postCommand = new TestCommand();

        getEndpoints.put(transformer.transform(path), getCommand);
        postEndpoints.put(transformer.transform(path), postCommand);

        HttpRequestEntity req = mock(HttpRequestEntity.class);
        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn(path);


        CommandResolverImpl postResolver = new CommandResolverImpl(postEndpoints, "POST", null);
        CommandResolverImpl getResolver = new CommandResolverImpl(getEndpoints, "GET", postResolver);

        assertSame(postCommand, getResolver.resolve(req));

    }

    @Test
    public void testCommandResolverWithVariable() throws CommandNotFoundException, PathVariableProcessingException {

        String path = "/foo/:name";
        ConcurrentMap<String, Command> endpoints = new ConcurrentSkipListMap<>();

        HttpRequestEntity req = mock(HttpRequestEntity.class);
        when(req.getMethod()).thenReturn("GET");
        when(req.getRequestURI()).thenReturn(path);

        Command command = new TestCommand();

        endpoints.put(transformer.transform(path), command);

        CommandResolverImpl resolver = new CommandResolverImpl(endpoints, "GET", null);

        assertSame(command, resolver.resolve(req));
    }







    private class TestCommand implements Command {

        @Override
        public Object execute(HttpRequestEntity req, HttpResponseEntity resp) throws CommandExecutionException, ServletException {
            return "Hello Test!";
        }
    }


}
