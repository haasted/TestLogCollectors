package dk.bitcraft.lc;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class JavaUtilLoggingTest {
    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(Logger.getLogger("test.logger"));

    @Test
    public void test() {
        {
            Logger log = Logger.getLogger("test.logger");
            log.warning("This is an warning!");
            log.warning("This is another warning!");
        }

        assertThat(collector.getLogs()).hasSize(2);
        List<String> logs = collector.getLogs();

        assertThat(logs.get(0)).contains("This is an warning!");
        assertThat(logs.get(1)).contains("This is another warning!");

        List<LogRecord> rawLogs = (List<LogRecord>) collector.getRawLogs();
        assertTrue(rawLogs.stream().allMatch(e -> e.getLevel() == Level.WARNING));
    }
}
