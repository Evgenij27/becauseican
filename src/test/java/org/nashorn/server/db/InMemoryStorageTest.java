package org.nashorn.server.db;

import org.junit.After;
import org.junit.Test;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.core.ScriptExecutionUnit;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class InMemoryStorageTest {

    private InMemoryStorage db = InMemoryStorage.instance();

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

    @After
    public void clear() throws UnitNotFoundException {
        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> units = db.getAllUnits();
        units.forEach(entry -> {
            try {
                db.delete(entry.getKey());
            } catch (UnitNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Test
    public void testReadSuccessfully() throws UnitNotFoundException {
        ScriptExecutionUnit write = newUnit();
        long id = db.create(write);
        ScriptExecutionUnit read = (ScriptExecutionUnit) db.read(id);
        assertSame(write, read);
    }

    @Test(expected = UnitNotFoundException.class)
    public void testReadUnsuccessfully() throws UnitNotFoundException {
        db.read(10L);
    }

    @Test(expected = UnitNotFoundException.class)
    public void testDelete() throws UnitNotFoundException{
        long id = db.create(newUnit());
        db.delete(id);
        db.read(id);
    }

    @Test(expected = UnitNotFoundException.class)
    public void testTryToDeleteNothing() throws UnitNotFoundException{
        db.delete(10L);
    }

    @Test
    public void testAllUnits() throws UnitNotFoundException {

        ScriptExecutionUnit unit1 = newUnit();
        ScriptExecutionUnit unit2 = newUnit();

        long id1 = db.create(unit1);
        long id2 = db.create(unit2);

        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> units = db.getAllUnits();
        assertEquals(2, units.size());

        assertSame(unit1, db.read(id1));
        assertSame(unit2, db.read(id2));
    }

    @Test
    public void testIncrement() {

        long id1 = db.create(newUnit());
        long id2 = db.create(newUnit());

        assertEquals(1L, (id2 - id1));
    }

    @Test
    public void testUpdate() throws UnitNotFoundException {
        ScriptExecutionUnit u = newUnit();
        long id = db.create(newUnit());
        db.update(id, u);
        assertSame(u, db.read(id));
    }
}
