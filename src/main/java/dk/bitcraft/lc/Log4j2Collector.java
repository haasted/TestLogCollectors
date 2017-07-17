package dk.bitcraft.lc;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
        return appender.events.stream().map(e -> e.toString()).collect(toList());
    }

    class ListAppender extends AbstractAppender {
        List<LogEvent> events = new ArrayList<>();

        ListAppender(String name) {
            super(name, null, PatternLayout.createDefaultLayout());
        }

        @Override
        public void append(LogEvent event) {
            events.add(event);
        }
    }
}
