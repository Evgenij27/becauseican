package org.nashorn.server.command;

import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;

import javax.servlet.ServletException;

public interface Command {

    void execute(HttpRequestEntity req, HttpResponsePublisher pub)
            throws CommandExecutionException, ServletException;
}
