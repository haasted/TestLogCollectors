package dk.bitcraft.lc;

import org.junit.Test;

import java.util.Optional;

import static org.testng.Assert.*;

public class LogBackCollectorTest {
    @Test
    public void getParentName() {
        assertEquals(LogBackCollector.getParentName("test.logger"), Optional.of("test"));
        assertEquals(LogBackCollector.getParentName("test.logger$sub"), Optional.of("test.logger"));
        assertEquals(LogBackCollector.getParentName(""), Optional.of("ROOT"));
        assertEquals(LogBackCollector.getParentName("ROOT"), Optional.empty());
        assertEquals(LogBackCollector.getParentName("root."), Optional.of("root"));
    }
}