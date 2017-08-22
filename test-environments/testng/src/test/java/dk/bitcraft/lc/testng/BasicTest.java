package dk.bitcraft.lc.testng;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import dk.bitcraft.lc.TestNGLogCollector;

import static org.assertj.core.api.Assertions.assertThat;

@Test
@Listeners(TestNGLogCollector.class)
public class BasicTest {
    private final static Logger log = LoggerFactory.getLogger(BasicTest.class);

    @BeforeTest
    public void captureLogger() {
        TestNGLogCollector.setLogSource(log);
    }

    public void isThisNowAtest() {
        log.info("This is an info statement");

        assertThat(TestNGLogCollector.getLogs()).hasSize(1).containsSubsequence("This is an info statement");
    }

    public void canIreturnStuff() {
        log.error("Error!");
        log.error("Another error!");
        assertThat(TestNGLogCollector.getLogs()).hasSize(2);
    }
}
