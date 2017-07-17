package dk.bitcraft.lc;

import org.apache.logging.log4j.Logger;

import java.util.List;

class Log4j2Collector implements CollectorImpl {
    private final Logger logger;

    public Log4j2Collector(Object logger) {
        this.logger = (Logger) logger;
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
