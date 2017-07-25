package dk.bitcraft.lc;


import org.slf4j.impl.JDK14LoggerAdapter;

import java.util.logging.Logger;

public class SLF4J_JavaUtilLoggingCollector {
    private SLF4J_JavaUtilLoggingCollector() { }

    public static JavaUtilLoggingCollector create(Object o) {
        JDK14LoggerAdapter adapter = (JDK14LoggerAdapter) o;
        Logger logger = Logger.getLogger(adapter.getName());
        return new JavaUtilLoggingCollector(logger);
    }
}
