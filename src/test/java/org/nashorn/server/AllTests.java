package org.nashorn.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nashorn.server.util.PathVariableSupplierTest;
import org.nashorn.server.util.RequestPathTransformerTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    PathVariableSupplierTest.class, RequestPathTransformerTest.class
})
public class AllTests {}
