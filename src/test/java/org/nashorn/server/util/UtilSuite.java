package org.nashorn.server.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PathVariableSupplierTest.class,
        RequestPathTransformerTest.class,
        ScriptEntityTest.class,
})
public class UtilSuite {
}
