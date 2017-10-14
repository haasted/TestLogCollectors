package dk.bitcraft.lc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class Log4j2Collector implements CollectorImpl {
    private final Logger logger;
    ListAppender appender;

    public Log4j2Collector(Object logger) {
        this.logger = (Logger) logger;
    }


    @Override
    public void setup() {
        appender = new ListAppender(UUID.randomUUID().toString());
        appender.start();

        logger.addAppender(appender);
    }

    @Override
    public void remove() {
        logger.removeAppender(appender);
    }

    @Override
    public List<String> getResult() {
        // Get a list of unique logger names
        Set<String> loggerNames = appender.events.stream().map(LogEvent::getLoggerName).collect(toSet());

        // Locate the most appropriate encoder for each logger.
        Map<String, Optional<Layout<?>>> encoders =
                loggerNames.stream()
                .collect(toMap(Function.identity(), Log4j2Collector::findEncoder));

        return appender.events.stream()
                .map(event -> new String(encoders.get(event.getLoggerName()).orElseGet(appender::getLayout).toByteArray(event)))
                .collect(toList());
    }

    private static Optional<Layout<?>> findEncoder(String loggername) {
        Logger logger = (Logger) LogManager.getLogger(loggername);

        // Locate first appender that writes to an outputstream.
        while (logger != null) {
            for (Appender app : logger.getAppenders().values()) {
                if (app instanceof AbstractOutputStreamAppender) {
                    Layout<? extends Serializable> layout = app.getLayout();
                    return Optional.of(layout);
                }
            }

            logger = logger.getParent();
        }

        return Optional.empty();
    }

    @Override
    public List<?> getRawLogs() {
        return appender.events.stream().collect(toList());
    }

    class ListAppender extends AbstractAppender {
        List<LogEvent> events = new ArrayList<>();


        ListAppender(String name) {
            super(name, null, PatternLayout.createDefaultLayout());
        }

        @Override
        public void append(LogEvent event) {
            events.add(event.toImmutable());
        }
    }
}
