package org.nashorn.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nashorn.server.util.PathVariableSupplierTest;
import org.nashorn.server.util.RequestPathTransformerTest;
import org.nashorn.server.util.ScriptEntityTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    PathVariableSupplierTest.class, RequestPathTransformerTest.class, ScriptEntityTest.class
})
public class AllTests {}
