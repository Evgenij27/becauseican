package org.nashorn.server;

import java.io.*;

public class SerDesUtils {

    public static <E> byte[] serialize(E object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }

    public static <E> E deserialize(byte[] buf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (E) ois.readObject();
    }
}
