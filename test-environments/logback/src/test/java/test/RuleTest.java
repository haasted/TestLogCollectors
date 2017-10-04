package test;

import org.junit.Rule;
import org.junit.Test;

import dk.bitcraft.lc.JUnit4LogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleTest {
    @Rule
    public JUnit4LogCollector logCollector = new JUnit4LogCollector(LoggerFactory.getLogger("test.logger"));

    @Test
    public void test() {
        {
            Logger logger = LoggerFactory.getLogger("test.logger");
            logger.warn("This is a warning!");
            logger.info("This is information!");
        }

        {
            Logger logger = LoggerFactory.getLogger("other.logger");
            logger.warn("This is a warning!");
            logger.info("This is information!");
        }

        List<String> logs = logCollector.getLogs();
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0)).contains("This is a warning!");
        assertThat(logs.get(1)).contains("This is information!");
    }


    // TODO Test exceptions

    @Test
    public void verifyAdditivity() {
        {
            Logger logger = LoggerFactory.getLogger("test.logger.subLogger");
            logger.warn("This should be available in the collector");
        }

        List<String> logs = logCollector.getLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0))
                .contains("This should be available in the collector - STDOUT Appender");
    }
}
