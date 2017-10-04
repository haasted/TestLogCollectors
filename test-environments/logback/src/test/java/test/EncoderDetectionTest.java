package test;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import dk.bitcraft.lc.JUnit4LogCollector;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify that encoders are picked out predictably.
 */
public class EncoderDetectionTest {
    @Rule
    public JUnit4LogCollector logCollector = new JUnit4LogCollector(LoggerFactory.getLogger("yet"));

    @Test
    public void verifyEncoderWhenAvailable() {
        Logger logger = LoggerFactory.getLogger("yet.testing");
        logger.warn("This is a warning!");

        List<String> logs = logCollector.getLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0)).endsWith("STDOUT Appender\n");
    }

    @Test
    public void useRoot() {
        Logger logger = LoggerFactory.getLogger("yet");
        logger.debug("This is a debug message.");

        List<String> logs = logCollector.getLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0)).endsWith("STDOUT Appender\n");
    }


    @Test
    public void findsCorrectEncoder() {
        Logger logger = LoggerFactory.getLogger("yet.another.test");
        logger.warn("This is a warning for the other appender!");

        List<String> logs = logCollector.getLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0)).endsWith("Other Appender\n");
    }

}
