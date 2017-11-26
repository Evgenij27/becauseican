package org.nashorn.server;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


@WebFilter(
        filterName = "ApiTypeFilter",
        servletNames = {"routerServlet"},
        urlPatterns = "/*",
        dispatcherTypes = {
                DispatcherType.REQUEST
        },
        asyncSupported = true)
public class ApiTypeFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ApiTypeFilter.class);

    private final Pattern pattern = Pattern.compile("/\\w*/(?<type>async|block)/v\\d{1}\\p{Punct}\\d{1}/\\w*");


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
	System.out.println("API_TYPE_FILTER");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        LOGGER.info("Requested uri: " + uri);

        Matcher matcher = pattern.matcher(uri);
        String apiType = null;

        if (!matcher.matches()) {
            LOGGER.error("Bad uri: " + uri);
            throw new ServletException("Bad request");
        }

        apiType = matcher.group("type");

        if (apiType == null || apiType.isEmpty()) {
            LOGGER.error("Bad api version: " + apiType);
            throw new ServletException("Bad request");
        }

        LOGGER.debug("Api version captured from uri: " + apiType);

        httpRequest.setAttribute("API_TYPE", apiType);

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
