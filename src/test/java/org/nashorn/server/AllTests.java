package org.nashorn.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nashorn.server.command.async.AsyncCommandSuite;
import org.nashorn.server.util.UtilSuite;
import org.nashorn.server.util.response.ResponseSuite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        AsyncCommandSuite.class,
        UtilSuite.class,
        ResponseSuite.class
})
public class AllTests {}
