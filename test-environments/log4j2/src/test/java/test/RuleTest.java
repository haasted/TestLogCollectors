package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import dk.bitcraft.lc.JUnit4LogCollector;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleTest {
    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(LogManager.getLogger("test.logger"));


    @Test
    public void test() {
        Logger logger = LogManager.getLogger("test.logger");
        logger.error("This is an error!");
        logger.error("This is another error!");


        assertThat(collector.getLogs()).hasSize(2);
        assertThat(collector.getLogs().get(0))
                .contains("This is an error!");
        assertThat(collector.getLogs().get(1))
                .contains("This is another error!");
    }


    @Test
    public void verifyAdditivity() {
        Logger logger = LogManager.getLogger("test.logger.subLogger");
        logger.error("This should be available in the collector");

        assertThat(collector.getLogs()).hasSize(1);
        assertThat(collector.getLogs().get(0))
                .contains("This should be available in the collector");
    }
}
