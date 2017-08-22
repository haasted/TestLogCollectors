package test;


import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.bitcraft.lc.JUnit4LogCollector;

import static org.assertj.core.api.Assertions.assertThat;

public class SLF4JTest {
    final static Logger logger = LoggerFactory.getLogger(SLF4JTest.class);

    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(logger);


    @Test
    public void test() {
        logger.error("This is an error!");
        logger.error("This is another error!");

        assertThat(collector.getLogs()).hasSize(2);
    }
}
