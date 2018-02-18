package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@WebFilter(
        filterName = "ConcurrencyFilter",
        urlPatterns = {"/api/*"},
        servletNames = "ApiServiceServlet"
)
public class ConcurrencyLimitFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ConcurrencyLimitFilter.class);

    private static final int CONCURRENCY_LIMIT = 10;

    private final Semaphore semaphore;

    public ConcurrencyLimitFilter() {
        int limit = CONCURRENCY_LIMIT;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            limit = (Integer) envCtx.lookup("limiter::limit");
            LOGGER.info("Injected limit size is: " + limit);
        } catch (NamingException ex) {
            LOGGER.error("Property not found. Use default value", ex);
        }
        semaphore = new Semaphore(limit);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        try {
            if (semaphore.tryAcquire(1, 2, TimeUnit.SECONDS)) {
                try {
                    LOGGER.info("START FILTERING");
                    chain.doFilter(request, response);
                    LOGGER.info("END FILTERING");
                } finally {
                    LOGGER.info("RELEASE");
                    semaphore.release(1);
                }
            } else {
                LOGGER.info("SERVICE UNAVAILABLE");
                HttpServletResponse httResponse = (HttpServletResponse) response;
                httResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.error(ex);
            throw new ServletException(ex);
        }
    }

    @Override
    public void destroy() {

    }
}
