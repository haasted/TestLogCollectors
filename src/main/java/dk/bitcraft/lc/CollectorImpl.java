package dk.bitcraft.lc;

import java.util.List;

interface CollectorImpl<LOG> {
    void setup();
    void remove();
    List<String> getResult();
    List<LOG> getRawLogs();
}
