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
        {
            Logger logger = LogManager.getLogger("test.logger");
            logger.error("This is an error!");
            logger.error("This is another error!");
        }

        assertThat(collector.getLogs())
                .hasSize(2)
                .contains("This is an error!", "This is another error!");
    }

}
