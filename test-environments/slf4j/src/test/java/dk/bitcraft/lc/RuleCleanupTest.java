package dk.bitcraft.lc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify that <code>System.err</code> get reinstated.
 */
public class RuleCleanupTest {
    @Test
    public void test() {
        final PrintStream originalSystemErr = System.err;

        Logger logger = LoggerFactory.getLogger(RuleCleanupTest.class);

        LogCollector collector = new LogCollector(logger);
        collector.before();

        logger.error("This is an error.");
        logger.error("This is another error.");

        assertThat(System.err).isNotSameAs(originalSystemErr);
        assertThat(collector.getLogs()).hasSize(2); // Verify that the collector works.

        collector.after();

        logger.error("This is one more error.");
        logger.error("The errors just keep coming");

        assertThat(System.err).isSameAs(originalSystemErr);
        assertThat(collector.getLogs()).hasSize(2); // Verify that collection has stopped.
    }
}
