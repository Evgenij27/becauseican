package org.nashorn.server;

import java.util.Collection;

public interface Traverser {

    void registerForTraverse(Traversable t);
    void registerForTraverse(Collection<? extends Traversable> c);

}
