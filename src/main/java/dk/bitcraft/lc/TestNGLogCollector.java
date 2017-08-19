package dk.bitcraft.lc;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestNGLogCollector implements ITestListener {

    private static CollectorImpl collector;

    public static void load(Object logger) {
        // TODO Collect this inside Frameworks class
        collector = Arrays.stream(Frameworks.values())
                .map(v -> v.getCollector(logger))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown logger " + logger.getClass()));
        System.out.println("Collector set : " + collector);
    }

    protected void before() {
        if (collector == null) {
            throw new IllegalStateException("TestNGLogCollector has not been properly initialized. Did you remember to call load()?");
        }

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
