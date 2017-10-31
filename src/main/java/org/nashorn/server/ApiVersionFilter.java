package org.nashorn.server;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

@WebFilter(
        filterName = "ApiVersionFilter",
        servletNames = {"routerServlet"},
        urlPatterns = "/*",
        dispatcherTypes = {
                DispatcherType.REQUEST
        },
        asyncSupported = true)
public class ApiVersionFilter  implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ApiVersionFilter.class);

    private final Set<String> allowedApiVersions = new HashSet<>();

    private final Pattern pattern = Pattern.compile("/\\w*/v(?<api>\\d{1}\\p{Punct}\\d{1})/\\w*");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("ApiVersionFilter constructed....");
        /*
            AllowedApiVersion fill from conf file
         */
        allowedApiVersions.add("0.9");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        LOGGER.info("Requested uri: " + uri);

        Matcher matcher = pattern.matcher(uri);
        String apiVersion = null;

        if (!matcher.matches()) {
            LOGGER.error("Api version sign was not found in uri");
            throw new ServletException("Bad request");
        }

        apiVersion = matcher.group("api");

        if (apiVersion == null || apiVersion.isEmpty()) {
            LOGGER.error("Bad api version: " + apiVersion);
            throw new ServletException("Bad request");
        }

        if (!allowedApiVersions.contains(apiVersion)) {
            LOGGER.error(String.format("This %s api version is not supported", apiVersion));
            throw new ServletException("Api version is not supported");
        }

        LOGGER.debug("Api version captured from uri: " + apiVersion);
        httpRequest.setAttribute("API_VERSION", apiVersion);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
