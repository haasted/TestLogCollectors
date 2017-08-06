package acme.test;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import acme.Gizmo;
import dk.bitcraft.lc.LogCollector;

import static org.junit.Assert.assertTrue;

public class GizmoTest {
    Logger logger = LogManager.getLogger("acme.Gizmo");

    @Rule
    public LogCollector collector = new LogCollector(logger);

    @Test
    public void testGizmo() {
        Gizmo gizmo = new Gizmo();
        gizmo.run();

        List<LogEvent> rawLogs = (List<LogEvent>) collector.getRawLogs();
        assertTrue(rawLogs.stream().noneMatch(l -> l.getLevel() == Level.ERROR));
    }
}
