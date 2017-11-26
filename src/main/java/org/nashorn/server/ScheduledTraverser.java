package org.nashorn.server;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.*;

public class ScheduledTraverser implements Traverser {

    private static final Logger LOGGER = Logger.getLogger(ScheduledTraverser.class);

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();
    private final CopyOnWriteArraySet<Traversable> units = new CopyOnWriteArraySet<>();

    public ScheduledTraverser(long initDelay, long delay, TimeUnit timeUnit) {
        executor.scheduleWithFixedDelay(buildTraverseTask(), initDelay, delay, timeUnit);
    }

    @Override
    public void registerForTraverse(Traversable t) {
        units.add(t);
    }

    @Override
    public void registerForTraverse(Collection<? extends Traversable> c) {
        units.addAll(c);
    }

    /*
        Awful method I think.
     */
    private Runnable buildTraverseTask() {
        return () -> {
            LOGGER.info("Traverse Task");
            LOGGER.info(units);
            for (Traversable unit : units) {
                unit.traverse();
            }
        };
    }


}
