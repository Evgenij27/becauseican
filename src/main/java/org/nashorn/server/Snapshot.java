package org.nashorn.server;

public class Snapshot {

    private static final int TAIL_OFFSET = 2;

    private final StringBuffer resultBuffer = new StringBuffer("{\"result\":\"\"}");
    //private final StringBuffer errorBuffer  = new StringBuffer("{\"error\":\"\"}");

    public static Snapshot newSnapshot(StringBuffer result) {
        return new Snapshot(result);
    }

    private Snapshot(StringBuffer result) {
        resultBuffer.insert(resultBuffer.length() - TAIL_OFFSET, result);
        //errorBuffer.insert(errorBuffer.length() - 1 - TAIL_OFFSET, error);
    }

    @Override
    public String toString() {
        return resultBuffer.toString();
    }
}
