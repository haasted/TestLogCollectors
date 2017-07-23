package dk.bitcraft.lc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class JavaUtilLoggingCollector implements CollectorImpl {

    private final Logger logger;
    private ListHandler handler;

    public JavaUtilLoggingCollector(Object logger) {
        this.logger = (Logger) logger;
    }

    @Override
    public void setup() {
        handler = new ListHandler();
        logger.addHandler(handler);

    }

    @Override
    public void remove() {
        logger.removeHandler(handler);
        handler.close();
    }

    @Override
    public List<String> getResult() {
        SimpleFormatter formatter = new SimpleFormatter();
        return handler.records.stream().map(formatter::format).collect(toList());
    }

    class ListHandler extends Handler {
        List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void close() throws SecurityException {
            records = null;
        }

        @Override
        public void flush() {}
    }
}
