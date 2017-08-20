package dk.bitcraft.lc;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TestNGLogCollector implements ITestListener {

    private static CollectorImpl collector;

    private static Object logger;

    public static void load(Object logger) {
        TestNGLogCollector.logger = Objects.requireNonNull(logger);
        // TODO Collect this inside Frameworks class
    }

    protected void before() {
        collector = Arrays.stream(Frameworks.values())
                .map(v -> v.getCollector(logger))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown logger " + logger.getClass()));

        collector.setup();
    }

    protected void after() {
        if (collector != null) {
            collector.remove();
            collector = null;
        }
    }

    public static List<String> getLogs() {
        return collector.getResult();
    }

    public static List<?> getRawLogs() {
        return collector.getRawLogs();
    }


    @Override
    public void onTestStart(ITestResult result) {
        before();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        after();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        after();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        after();
    }

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}
