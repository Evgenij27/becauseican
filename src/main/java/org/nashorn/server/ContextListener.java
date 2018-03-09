package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.command.async.CancelAndDeleteExecutionByIdAsyncCommand;
import org.nashorn.server.command.async.GetAllScriptsAsyncCommand;
import org.nashorn.server.command.async.GetScriptByIdAsyncCommand;
import org.nashorn.server.command.async.SubmitNewScriptAsyncCommand;
import org.nashorn.server.command.block.SubmitNewScriptBlockCommand;
import org.nashorn.server.handler.Handler;
import org.nashorn.server.handler.HandlerChainImpl;
import org.nashorn.server.handler.RequestHandlerBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;


@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOGGER.info("Context initialized.");

        ServletContext context = servletContextEvent.getServletContext();
        LOGGER.info("Context is =========> " + context);

        registerServlet(context);

    }

    private Handler buildBlockHandler() {
        RequestHandlerBuilder blockBuilder = new RequestHandlerBuilder("/nashorn/api/v0.9/block");

        /* POST Endpoints */
        blockBuilder.postEndpoint(ApiEndpoints.SCRIPT, new SubmitNewScriptBlockCommand());

        return blockBuilder.build();
    }

    private Handler buildAsyncHandler() {
        RequestHandlerBuilder asyncBuilder = new RequestHandlerBuilder("/nashorn/api/v0.9/async");

        /* GET Endpoints */
        asyncBuilder.getEndpoint(ApiEndpoints.SCRIPT_BY_ID, new GetScriptByIdAsyncCommand());
        asyncBuilder.getEndpoint(ApiEndpoints.SCRIPT, new GetAllScriptsAsyncCommand());

        /* POST Endpoints */
        asyncBuilder.postEndpoint(ApiEndpoints.SCRIPT, new SubmitNewScriptAsyncCommand());

        /* DELETE Endpoints */
        asyncBuilder.deleteEndpoint(ApiEndpoints.SCRIPT_BY_ID, new CancelAndDeleteExecutionByIdAsyncCommand());

        return asyncBuilder.build();
    }

    private void registerServlet(ServletContext context) {
        HandlerChainImpl asyncChain = new HandlerChainImpl(buildAsyncHandler(), null);
        HandlerChainImpl startChain = new HandlerChainImpl(buildBlockHandler(), asyncChain);

        ApiGatewayServlet ags = new ApiGatewayServlet(startChain);

        ServletRegistration.Dynamic reg = context.addServlet("ApiServiceServlet", ags);
        reg.addMapping("/api/*");
        reg.setLoadOnStartup(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*
            Graceful shutdown will be later
         */
        LOGGER.info("Context destroyed.");
    }
}
