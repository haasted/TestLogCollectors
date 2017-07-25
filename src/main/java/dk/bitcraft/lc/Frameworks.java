package dk.bitcraft.lc;

import java.util.Optional;
import java.util.function.Function;

enum Frameworks {

    Logback("ch.qos.logback.classic.Logger", LogBackCollector::new),
    Log4j2("org.apache.logging.log4j.Logger", Log4j2Collector::new),
    JavaUtilLogging("java.util.logging.Logger", JavaUtilLoggingCollector::new),
    Log4j2_slf4j("org.apache.logging.slf4j.Log4jLogger", SLF4J_Log4j2Collector::create);

    private final Optional<Class> clazz;
    private final Function<Object, CollectorImpl> creator;

    Frameworks(String clazz, Function<Object, CollectorImpl> creator) {
        this.clazz = lookFor(clazz);
        this.creator = creator;
    }

    Optional<CollectorImpl> getCollector(final Object logger) {
        return clazz
                .filter(c -> c.isInstance(logger))
                .map(__ -> creator.apply(logger));
    }

    private static Optional<Class> lookFor(String s) {
        try {
            return Optional.of(Class.forName(s));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
