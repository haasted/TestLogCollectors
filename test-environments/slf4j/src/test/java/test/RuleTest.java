package test;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.bitcraft.lc.JUnit4LogCollector;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleTest {
    final static Logger logger = LoggerFactory.getLogger(RuleTest.class);

    @Rule
    public JUnit4LogCollector<String> collector = new JUnit4LogCollector<>(logger);


    @Test
    public void test() {

        logger.error("This is the first error");
        logger.error("This is an error with an exception attached!", new RuntimeException("Errrd!"));

        assertThat(collector.getLogs()).hasSize(2);

        assertThat(collector.getLogs().get(0)).contains("This is the first error");
        assertThat(collector.getLogs().get(1))
                .contains("This is an error with an exception attached!")
                .contains("java.lang.RuntimeException");
    }
}
