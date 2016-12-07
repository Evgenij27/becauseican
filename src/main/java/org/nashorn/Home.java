package org.nashorn;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "home", urlPatterns ={"/*"}, asyncSupported = true)
public class Home extends GenericServlet {

    @Override
    protected void service(ServletRequest req, ServletResponse resp)
        throws IOException, ServletException {

        AsyncContext actx = req.startAsync();
        
    }

}
