package org.nashorn.server.db;

import org.junit.Test;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.core.ScriptExecutionUnit;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class InMemoryStorageTest {

    private ScriptExecutionUnit newUnit() {
        ExecutionUnitPool pool = ExecutionUnitPool.instance();
        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript script = null;
        try {
            script = compiler.compile("print('Hello!');");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return pool.evalAsync(script);
    }

    @Test
    public void testCreate() {
        InMemoryStorage db = InMemoryStorage.instance();
        assertEquals(1L, db.create(newUnit()));
    }

    @Test
    public void testReadSuccessfully() throws UnitNotFoundException {
        InMemoryStorage db = InMemoryStorage.instance();
        ScriptExecutionUnit write = newUnit();
        long id = db.create(write);
        ScriptExecutionUnit read = (ScriptExecutionUnit) db.read(id);
        assertSame(write, read);
    }

    @Test(expected = UnitNotFoundException.class)
    public void testReadUnsuccessfully() throws UnitNotFoundException {
        InMemoryStorage db = InMemoryStorage.instance();
        db.read(10L);
    }

    @Test(expected = UnitNotFoundException.class)
    public void testDelete() throws UnitNotFoundException{
        InMemoryStorage db = InMemoryStorage.instance();
        long id = db.create(newUnit());
        db.delete(id);
        db.read(id);
    }
}
