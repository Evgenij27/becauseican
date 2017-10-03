package org.nashorn.server;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.util.Enumeration;
import java.util.Hashtable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;


public class ExecutorServiceFactory implements ObjectFactory {

    private static final Logger LOGGER = Logger.getLogger(ExecutorServiceFactory.class);

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
            throws Exception {

        LOGGER.info("Taking object from JNDI");
        Reference ref = (Reference) obj;
        Enumeration<RefAddr> addrs = ref.getAll();
        int nThreads = 0;
        while (addrs.hasMoreElements()) {
            RefAddr addr = addrs.nextElement();
            String typeName = addr.getType();
            String value = (String) addr.getContent();
            LOGGER.debug(String.format("TypeName obtained form context : %s", typeName));
            LOGGER.debug(String.format("TypeValue obtained form context : %s", value));
            /* Finding nThreads parameter */
            if (typeName.equals("nThreads")) {
                try {
                    nThreads = Integer.parseInt(value);
                    LOGGER.debug(String.format("Parsed value of %s is : %d", typeName, nThreads));
                } catch (NumberFormatException ex) {
                    LOGGER.error(ex);
                    throw new NamingException("Invalid value");
                }
            }
        }


        return Executors.newFixedThreadPool(nThreads);
    }
}
