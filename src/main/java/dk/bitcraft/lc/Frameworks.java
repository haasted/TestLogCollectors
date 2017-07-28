package dk.bitcraft.lc;

import java.util.Optional;
import java.util.function.Function;

enum Frameworks {

    Logback("ch.qos.logback.classic.Logger", v -> new LogBackCollector(v)),
    Log4j2("org.apache.logging.log4j.Logger", v -> new Log4j2Collector(v)),
    JavaUtilLogging("java.util.logging.Logger", v -> new JavaUtilLoggingCollector(v)),
    Log4j2_slf4j("org.apache.logging.slf4j.Log4jLogger", v -> SLF4J_Log4j2Collector.create(v)),
    javaUtilLogging_slf4j("org.slf4j.impl.JDK14LoggerAdapter", v -> SLF4J_JavaUtilLoggingCollector.create(v)),
    slf4jSimple("org.slf4j.impl.SimpleLogger", __ -> new SLF4JSimpleCollector());

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
