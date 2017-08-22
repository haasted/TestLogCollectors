package dk.bitcraft.lc;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class LogbackTest {
    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(LoggerFactory.getLogger("test.logger"));

    @Test
    public void test() {
        {
            final Logger logger = LoggerFactory.getLogger("should.not.be.collected");
            logger.warn("This should not be collected!");
        }

        {
            final Logger logger = LoggerFactory.getLogger("test.logger");
            logger.warn("This should be collected!");
        }

        assertThat(collector.getLogs()).hasSize(1);
        assertThat(collector.getLogs().get(0)).contains("This should be collected!");

        List<LoggingEvent> rawLogs = (List<LoggingEvent>) collector.getRawLogs();
        assertTrue(rawLogs.stream().allMatch(e -> e.getLevel() == Level.WARN));
    }
}
