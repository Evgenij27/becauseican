package org.nashorn.server.util.response;


import org.junit.Test;
import static org.mockito.Mockito.*;

import org.nashorn.server.core.ScriptExecutionUnit;

public class ScriptUnitDataTest {

    @Test
    public void testScriptUnitDataWithoutError() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);
        when(unit.finishedExceptionally()).thenReturn(false);
        ScriptUnitData data = new ScriptUnitData(unit);
    }

    @Test
    public void testScriptUnitDataWithError() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);
        when(unit.finishedExceptionally()).thenReturn(true);
        ScriptUnitData data = new ScriptUnitData(unit);
    }
}
