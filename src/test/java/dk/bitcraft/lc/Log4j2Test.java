package dk.bitcraft.lc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Log4j2Test {
    @Rule
    public LogCollector collector = new LogCollector(LogManager.getLogger("test.logger"));


    @Test
    public void test() {
        {
            Logger logger = LogManager.getLogger("test.logger");
            logger.error("This is an error!");
            logger.error("This is another error!");
            logger.error("This is a third error!");
        }

        assertThat(collector.getLogs()).hasSize(3)
                .contains("This is an error!", "This is another error!", "This is a third error!");

        List<?> rawLogs = collector.getRawLogs();
        assertThat(rawLogs).hasSize(3);

        System.out.println(rawLogs.get(0).getClass());
    }
}
