package org.nashorn.server.command;

import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;

import javax.servlet.ServletException;

public interface Command {

    Object execute(HttpRequestEntity req, HttpResponseEntity resp)
            throws CommandExecutionException, ServletException;
}
