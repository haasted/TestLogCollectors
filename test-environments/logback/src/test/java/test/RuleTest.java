package test;

import org.junit.Rule;
import org.junit.Test;

import dk.bitcraft.lc.JUnit4LogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        assertThat(logCollector.getLogs())
                .hasSize(2)
                .contains("This is a warning!", "This is information!");
    }

    @Test
    public void verifyAdditivity() {
        {
            Logger logger = LoggerFactory.getLogger("test.logger.subLogger");
            logger.warn("This should be available in the collector");
        }

        assertThat(logCollector.getLogs())
                .hasSize(1)
                .contains("This should be available in the collector");
    }
}
