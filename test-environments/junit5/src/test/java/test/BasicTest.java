package test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.bitcraft.lc.JUnit5LogCollector;
import dk.bitcraft.lc.LogCollectorExtension;

import static org.assertj.core.api.Assertions.assertThat;

@LogCollectorExtension
public class BasicTest {
    public JUnit5LogCollector logCollector1 = new JUnit5LogCollector(LoggerFactory.getLogger("acme.gizmo"));

    private JUnit5LogCollector logCollector2 = new JUnit5LogCollector(LoggerFactory.getLogger("dingenot"));

    @Test
    void test1() {
        Logger log = LoggerFactory.getLogger("acme.gizmo");
        log.warn("This is a warning");
        log.info("This is a piece of information");

        assertThat(logCollector1.getLogs()).hasSize(2);
        assertThat(logCollector2.getLogs()).isEmpty();
    }

    @Test
    void test2() {
        assertThat(logCollector1.getLogs()).isEmpty();
        assertThat(logCollector2.getLogs()).isEmpty();
    }
}
