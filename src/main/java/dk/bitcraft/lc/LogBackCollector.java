package dk.bitcraft.lc;

import java.util.List;
import java.util.UUID;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static java.util.stream.Collectors.toList;

class LogBackCollector implements CollectorImpl<ILoggingEvent> {
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
        return appender.list.stream().map(e -> e.getFormattedMessage()).collect(toList());
    }

    @Override
    public List<ILoggingEvent> getRawLogs() {
        return appender.list.stream().collect(toList());
    }
}
