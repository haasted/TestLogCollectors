package dk.bitcraft.lc;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaUtilLoggingTest {
    @Rule
    public LogCollector collector = new LogCollector(Logger.getLogger("test.logger"));

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
    }
}
