package org.nashorn.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.command.Command;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


@WebServlet(name = "home", urlPatterns = {"/*"}, loadOnStartup = 1, asyncSupported = true)
public class HomeServlet extends HttpServlet {

    private final AtomicLong counter = new AtomicLong();

    private final Map<Long, AsyncContext> clients = new TreeMap<>();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("SERVICE");

        RequestObject ro = processRequest(req);

        ObjectMapper mapper = new ObjectMapper();
        String roJson = mapper.writeValueAsString(ro);
        System.out.println(roJson);

        RequestObject roObjectFromJson = mapper.readValue(roJson, RequestObject.class);
        System.out.println(roObjectFromJson);
    }

    private RequestObject processRequest(HttpServletRequest httpRequest) throws ServletException {
        RequestObject requestObject = new RequestObject();
        requestObject.setId(counter.getAndIncrement());

        requestObject.setUri(httpRequest.getRequestURI());
        requestObject.setMethod(httpRequest.getMethod());

        Map<String, String> headers = processHeaders(httpRequest);
        requestObject.setHeaders(headers);

        requestObject.setBody(processBody(httpRequest));

        return requestObject;
    }

    private Map<String, String> processHeaders(HttpServletRequest httpRequest) {
        Map<String, String> headerMap = new TreeMap<>();
        Enumeration<String> headersName = httpRequest.getHeaderNames();

        while (headersName.hasMoreElements()) {
            String headerName = headersName.nextElement();
            headerMap.put(headerName, httpRequest.getHeader(headerName));
        }

        return headerMap;
    }

    private ScriptEntity processBody(HttpServletRequest httpRequest) throws ServletException {
        ScriptEntity body = null;
        ObjectMapper mapper = new ObjectMapper();

        int contentLength = httpRequest.getIntHeader("Content-Length");

        if (contentLength > 0) {
            try (final BufferedReader reader = httpRequest.getReader()) {
                body = mapper.readValue(reader, ScriptEntity.class);
            } catch (IOException ex) {
                throw new ServletException(ex);
            }
        }
        return body;
    }
}
