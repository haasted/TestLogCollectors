package dk.bitcraft.lc;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class LogbackTest {
    @Rule
    public LogCollector collector = new LogCollector(LoggerFactory.getLogger("test.logger"));

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

        assertThat(collector.getLogs())
                .containsExactly("This should be collected!");
    }
}
