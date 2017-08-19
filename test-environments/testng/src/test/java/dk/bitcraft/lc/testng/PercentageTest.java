package dk.bitcraft.lc.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import dk.bitcraft.lc.TestNGLogCollector;

import static org.assertj.core.api.Assertions.assertThat;

@Test(successPercentage = 80, invocationCount = 10)
@Listeners(TestNGLogCollector.class)
public class PercentageTest {
    int invocationCount;

    private final static Logger log = LoggerFactory.getLogger(PercentageTest.class);

    @BeforeMethod
    public void captureLogger() {
        TestNGLogCollector.load(log);
    }

    // Verify that clean up also works with a test that is allowed to occasionally fail
    public void sometimesFails() {
        log.warn("This is a warning.");
        log.info("This is purely informational.");

        List<LoggingEvent> logs = (List<LoggingEvent>) TestNGLogCollector.getRawLogs();

        assertThat(logs)
                .hasSize(2)
                .anySatisfy(e -> assertThat(e.getLevel()).isEqualTo(Level.WARN))
                .anySatisfy(e -> assertThat(e.getLevel()).isEqualTo(Level.INFO));

        invocationCount++;

        if (invocationCount > 8) {
            Assert.fail();
        }
    }
}
