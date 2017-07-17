package dk.bitcraft.lc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class LogBackCollector {
    private final Logger logger;

    private final static String appenderName = UUID.randomUUID().toString();

    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

    public LogBackCollector(Logger logger) {
        this.logger = logger;
    }

    public void setup() {
        appender.setName(appenderName);
        appender.start();

        logger.addAppender(appender);
    }

    public void remove() {
        logger.detachAppender(appenderName);


        appender.stop();
    }

    List<String> getResult() {
        return appender.list.stream().map(e -> e.getFormattedMessage()).collect(Collectors.toList());
    }
}
