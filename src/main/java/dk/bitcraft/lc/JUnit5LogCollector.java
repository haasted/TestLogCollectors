package dk.bitcraft.lc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JUnit5LogCollector {
    private CollectorImpl collector;

    public JUnit5LogCollector(Object logger) {
        collector = Arrays.stream(Frameworks.values())
                .map(v -> v.getCollector(logger))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown logger " + logger.getClass()));
    }

    void beforeTest() {
        collector.setup();
    }

    void afterTest() {
        collector.remove();
    }

    public List<String> getLogs() {
        return collector.getResult();
    }

    public List<?> getRawLogs() {
        return collector.getRawLogs();
    }
}
