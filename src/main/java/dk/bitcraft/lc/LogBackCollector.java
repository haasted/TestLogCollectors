package dk.bitcraft.lc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.read.ListAppender;

import static java.util.stream.Collectors.toSet;

class LogBackCollector implements CollectorImpl {
    private final Logger logger;

    private final static String appenderName = UUID.randomUUID().toString();

    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

    public LogBackCollector(Object logger) {
        this.logger = (Logger) logger;
    }

    @Override
    public void setup() {

        appender.setName(appenderName);
        appender.start();

        logger.addAppender(appender);
    }

    @Override
    public void remove() {
        logger.detachAppender(appenderName);
        appender.stop();
    }

    @Override
    public List<String> getResult() {
        // Get a list of unique logger names
        Set<String> loggerNames = appender.list.stream().map(ILoggingEvent::getLoggerName).collect(toSet());

        final LoggerContext ctx = logger.getLoggerContext();
        // Locate the most appropriate encoder for each logger.
        Map<String, Optional<Encoder<ILoggingEvent>>> encoders
            = loggerNames.stream().map(ctx::exists).filter(Objects::nonNull)
                .collect(Collectors.toMap(Logger::getName, LogBackCollector::findEncoder));


        // Write each event with an appropriate encoder or fall back to just displaying its message.
        List<String> result = new ArrayList<>();
        for (ILoggingEvent event : appender.list) {
            result.add(encoders.get(event.getLoggerName())
                        .map(enc -> new String(enc.encode(event)))
                        .orElseGet(event::getFormattedMessage));
        }
        return result;
    }

    @Override
    public List<?> getRawLogs() {
        return Collections.unmodifiableList(appender.list);
    }

    static Optional<Encoder<ILoggingEvent>> findEncoder(Logger logger) {
        LoggerContext ctx = logger.getLoggerContext();
        Optional<Logger> l = Optional.of(logger);
        Optional<Encoder<ILoggingEvent>> encoder = Optional.empty();

        while (l.isPresent()) {
            encoder = l.flatMap(LogBackCollector::extractEncoderFromLogger);

            if (encoder.isPresent())
                break;

            l = findParent(ctx, l.get());
            // TODO Stop iteration if logger is not additive
        }

        return encoder;
    }

    static Optional<Encoder<ILoggingEvent>> extractEncoderFromLogger(Logger logger) {
        Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();

        while (it.hasNext()) {
            Appender<ILoggingEvent> app = it.next();
            if (app instanceof OutputStreamAppender) {
                return Optional.ofNullable(((OutputStreamAppender) app).getEncoder());
            }
        }

        return Optional.empty();

    }

    static Optional<Logger> findParent(LoggerContext ctx, Logger logger) {
        Optional<String> name = Optional.of(logger.getName());
        while (name.isPresent()) {
            name = getParentName(name.get());
            Optional<Logger> parent = name.map(ctx::exists);
            if (parent.isPresent()) {
                return parent;
            }
        }

        return Optional.empty();
    }

    static Optional<String> getParentName(CharSequence loggerName) {
        if (Logger.ROOT_LOGGER_NAME.equals(loggerName)) {
            return Optional.empty();
        }

        // Adapted from ch.qos.logback.classic.util.LoggerNameUtil#getFirstSeparatorIndexOf
        StringBuilder sb = new StringBuilder(loggerName).reverse();
        for (int i = 0 ; i < sb.length() ; i++) {
            char c = sb.charAt(i);
            if (c == CoreConstants.DOT || c == CoreConstants.DOLLAR) {
                String result = sb.replace(0, i + 1, "").reverse().toString();
                return Optional.of(result);
            }
        }

        return Optional.of(Logger.ROOT_LOGGER_NAME);
    }
}
