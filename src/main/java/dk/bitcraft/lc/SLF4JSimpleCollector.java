package dk.bitcraft.lc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>SLF4J's SimpleLogger works by printing directly to System.err, so this collector assumes control of System.err and captures its output.</p>
 * <p>Any other usage of <code>System.err</code> will be included among the captured log statements.</p>
 */
public class SLF4JSimpleCollector implements CollectorImpl {

    private PrintStream systemErr;
    private CapturingPrintStream capturingPrintStream;

    @Override
    public void setup() {
        systemErr = System.err;
        capturingPrintStream = new CapturingPrintStream(systemErr);
        System.setErr(capturingPrintStream);
    }

    @Override
    public void remove() {
        System.setErr(systemErr);
    }

    @Override
    public List<String> getResult() {
        capturingPrintStream.flush();
        List<String> result = capturingPrintStream.logs.stream().collect(toList());
        java.util.Collections.reverse(result);
        return result;
    }

    class CapturingPrintStream extends PrintStream {
        Deque<String> logs = new LinkedList<>();

        private StringBuilder sb;

        public CapturingPrintStream(OutputStream out) {
            super(out);
        }

        @Override
        public void flush() {
            if (sb != null) {
                logs.push(sb.toString());
                sb = null;
            }
        }

        @Override
        public void println(String s) {
            if (sb != null) {
                logs.push(sb.toString());
                sb = null;
            }

            logs.push(s);
        }

        @Override
        public void println(Object o) {
            if (sb == null) {
                sb = new StringBuilder(logs.pop());
            }
            sb.append('\n');
            sb.append(String.valueOf(o));
        }
    }
}
