package org.nashorn.server.util.response;


import org.junit.Test;
import static org.mockito.Mockito.*;

import org.nashorn.server.core.ScriptExecutionUnit;

import java.io.StringWriter;

public class ScriptUnitDataTest {

    @Test
    public void testScriptUnitDataWithoutError() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);

        when(unit.finishedExceptionally()).thenReturn(false);
        when(unit.getResultOutput()).thenReturn(new StringWriter());
        when(unit.getErrorOutput()).thenReturn(new StringWriter());
        when(unit.getCause()).thenReturn(new Throwable("Exception : Hello Exception"));

        ScriptUnitData data = new ScriptUnitData(unit);
    }

    @Test
    public void testScriptUnitDataWithError() {
        ScriptExecutionUnit unit = mock(ScriptExecutionUnit.class);

        when(unit.finishedExceptionally()).thenReturn(true);
        when(unit.getResultOutput()).thenReturn(new StringWriter());
        when(unit.getErrorOutput()).thenReturn(new StringWriter());
        when(unit.getCause()).thenReturn(new Throwable("Exception : Hello Exception"));

        ScriptUnitData data = new ScriptUnitData(unit);
    }
}
