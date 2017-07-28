package dk.bitcraft.lc;

import java.util.List;

interface CollectorImpl {
    void setup();
    void remove();
    List<String> getResult();
    List<?> getRawLogs();
}
