package org.nashorn.server.async.command;

import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.PathVariableNotFoundException;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ResponseMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GreetingAsyncCommand implements Command {

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);

        String name = null;
        try {
            name = pvs.supplyAsString("name");
        } catch (PathVariableNotFoundException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }

        return new ResponseMessage(String.format("Hello, %s", name));
    }
}
