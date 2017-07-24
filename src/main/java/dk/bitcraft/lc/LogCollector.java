package dk.bitcraft.lc;

import org.junit.rules.ExternalResource;

import java.util.List;
import java.util.Optional;


public class LogCollector extends ExternalResource {
    private CollectorImpl collector;

    // TODO Turn into an enum?
    private static final Optional<Class> log4j2 = lookFor("org.apache.logging.log4j.Logger");

    private static final Optional<Class> Logback = lookFor("ch.qos.logback.classic.Logger");

    private static final Optional<Class> javaUtilLogging = lookFor("java.util.logging.Logger");

    private static final Optional<Class> slf4jLog4j2 = lookFor("org.apache.logging.slf4j.Log4jLogger");



    private static Optional<Class> lookFor(String s) {
        try {
            return Optional.of(Class.forName(s));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public LogCollector(Object logger) {
        if (Logback.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = new LogBackCollector(logger);
            return;
        }

        if (log4j2.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = new Log4j2Collector(logger);
            return;
        }

        if (slf4jLog4j2.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = SLF4J_Log4j2Collector.create(logger);
            return;
        }

        if (javaUtilLogging.filter(c -> c.isInstance(logger)).isPresent()) {
            collector = new JavaUtilLoggingCollector(logger);
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
