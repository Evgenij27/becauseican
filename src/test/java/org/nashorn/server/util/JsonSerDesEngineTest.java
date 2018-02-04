package org.nashorn.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

import static org.junit.Assert.*;

public class JsonSerDesEngineTest {

    @Test
    public void testWriteObject() throws JsonProcessingException {
        TestObject to = new TestObject();
        to.setId(10L);
        to.setName("Yevhenii");
        assertEquals(buildString(), JsonSerDesEngine.writeEntity(to));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteObjectWithException() throws JsonProcessingException {
        TestObjectShouldFail f = new TestObjectShouldFail("ABC");
        JsonSerDesEngine.writeEntity(f);
    }

    @Test
    public void testReadObject() throws IOException {
        PipedReader rd = new PipedReader();
        PipedWriter wr = new PipedWriter(rd);
        wr.write(getScript());
        ScriptEntity se = JsonSerDesEngine.readEntity(rd);
        assertEquals("print('Hello World!')", se.getScript());
    }

    private String getScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"script\"");
        sb.append(":");
        sb.append("\"print('Hello World!')\"");
        sb.append("}");
        return sb.toString();
    }


    private String buildString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"name\" : \"Yevhenii\",\n");
        sb.append("  \"id\" : 10\n");
        sb.append("}");
        return sb.toString();

    }

    private class TestObject {
        private String name;
        private long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    private class TestObjectShouldFail {
        private final String name;

        private TestObjectShouldFail(String name) {
            this.name = name;
        }
    }
}
