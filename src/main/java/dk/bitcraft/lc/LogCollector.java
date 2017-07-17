package dk.bitcraft.lc;

import org.junit.rules.ExternalResource;

import java.util.List;
import java.util.Optional;


public class LogCollector extends ExternalResource {
    private CollectorImpl collector;

    private static final Optional<Class> log4j2 = lookFor("org.apache.logging.log4j.Logger");

    private static final Optional<Class> Logback = lookFor("ch.qos.logback.classic.Logger");



    private static Optional<Class> lookFor(String s) {
        try {
            return Optional.of(Class.forName(s));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public LogCollector(Object logger) {
        // TODO Turn this into a String comparison for the cases where logback is not on the classpath
        if (Logback.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = new LogBackCollector(logger);
            return;
        }

        if (log4j2.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = new Log4j2Collector(logger);
            return;
        }

        throw new RuntimeException("Unknown logger " + logger.getClass());
    }

    @Override
    protected void before() {
        collector.setup();
    }

    @Override
    protected void after() {
        collector.remove();
    }

    public List<String> getLogs() {
        return collector.getResult();
    }
}
