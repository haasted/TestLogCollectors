package dk.bitcraft.lc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class Log4j2Test {
    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(LogManager.getLogger("test.logger"));

    @Test
    public void test() {
        {
            Logger logger = LogManager.getLogger("test.logger");
            logger.error("This is an error!");
            logger.error("This is another error!");
            logger.error("This is a third error!");
        }

        assertThat(collector.getLogs()).hasSize(3);
        assertThat(collector.getLogs().get(0)).contains("This is an error!");
        assertThat(collector.getLogs().get(1)).contains("This is another error!");
        assertThat(collector.getLogs().get(2)).contains("This is a third error!");

        List<LogEvent> rawLogs = (List<LogEvent>) collector.getRawLogs();
        assertThat(rawLogs).hasSize(3);

        assertTrue(rawLogs.stream().allMatch(l -> l.getLevel() == Level.ERROR));
    }
}
