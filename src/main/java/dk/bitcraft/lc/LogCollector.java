package dk.bitcraft.lc;

import org.junit.rules.ExternalResource;

import java.util.List;


public class LogCollector extends ExternalResource {
    private LogBackCollector collector;

    public LogCollector(org.slf4j.Logger logger) {
        // TODO Turn this into a String comparison for the cases where logback is not on the classpath
        if (logger instanceof ch.qos.logback.classic.Logger) {
            collector = new LogBackCollector((ch.qos.logback.classic.Logger) logger);
        }
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
