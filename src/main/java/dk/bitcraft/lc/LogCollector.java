package dk.bitcraft.lc;

import org.junit.rules.ExternalResource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class LogCollector extends ExternalResource {
    private CollectorImpl collector;

    public LogCollector(Object logger) {
        collector = Arrays.stream(Frameworks.values())
                .map(v -> v.getCollector(logger))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown logger " + logger.getClass()));
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

    public List<?> getRawLogs() {
        return collector.getRawLogs();
    }
}