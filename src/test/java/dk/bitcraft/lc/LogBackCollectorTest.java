package dk.bitcraft.lc;

import org.junit.Test;

import static org.testng.Assert.*;

public class LogBackCollectorTest {
    @Test
    public void getParentName() {
        assertEquals(LogBackCollector.getParentName("test.logger"), "test");
        assertEquals(LogBackCollector.getParentName("test.logger$sub"), "test.logger");
        assertEquals(LogBackCollector.getParentName(""), null);
        assertEquals(LogBackCollector.getParentName("root"), null);
        assertEquals(LogBackCollector.getParentName("root."), "root");
    }
}