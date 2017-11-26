package org.nashorn.server.block;

import org.apache.log4j.Logger;

import org.nashorn.server.Command;
import org.nashorn.server.CommandResolver;
import org.nashorn.server.CommandRouter;
import org.nashorn.server.block.command.GreetingBlockCommand;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(
        filterName = "BlockCommandResolverFilter",
        servletNames = {"blockServlet"},
        dispatcherTypes = {DispatcherType.REQUEST},
        asyncSupported = true)
public class BlockCommandResolverFilter implements Filter{

    private static final Logger LOGGER = Logger.getLogger(BlockCommandResolverFilter.class);

    private final ScheduledExecutionUnitPool pool = ScheduledExecutionUnitPool.instance();

    private final CommandResolver resolver;

    {
        CommandRouter router = new CommandRouter("/nashorn/block/v0.9");
        router.post
                ("/", new GreetingBlockCommand(pool));
        resolver = new CommandResolver(router.registry());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Start filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Command command = resolver.resolve(httpRequest);
        request.setAttribute("COMMAND", command);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("End filter");
    }
}
