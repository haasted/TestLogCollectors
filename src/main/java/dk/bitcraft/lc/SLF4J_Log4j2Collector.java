package dk.bitcraft.lc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.slf4j.Log4jLogger;

public class SLF4J_Log4j2Collector {
    private SLF4J_Log4j2Collector() {}

    public static Log4j2Collector create(Object l) {
        Log4jLogger loggerWrapper = (Log4jLogger) l;
        Logger logger = LogManager.getLogger(loggerWrapper.getName());
        return new Log4j2Collector(logger);
    }
}
