package org.nashorn.server.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScriptEntityTest {

    private static final String SCRIPT = "{\"script\" : \"print(\"Hello, JS!\");\"}";

    @Test
    public void testScriptEntity() {
        ScriptEntity se = new ScriptEntity(SCRIPT);
        assertEquals(SCRIPT, se.getScript());
    }
}
