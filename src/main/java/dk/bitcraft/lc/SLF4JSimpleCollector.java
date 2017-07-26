package dk.bitcraft.lc;

import org.slf4j.impl.SimpleLogger;

import java.util.List;

public class SLF4JSimpleCollector implements CollectorImpl {
    private SimpleLogger logger;

    public SLF4JSimpleCollector(Object l) {
        logger = (SimpleLogger) l;
        logger.
// TODO Nasty stuff! Override system.err and attempt to catch the output of simplelogger...
    }

    @Override
    public void setup() {

    }

    @Override
    public void remove() {

    }

    @Override
    public List<String> getResult() {
        return null;
    }
}
