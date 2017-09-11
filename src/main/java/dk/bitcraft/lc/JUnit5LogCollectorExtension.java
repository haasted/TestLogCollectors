package dk.bitcraft.lc;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

class JUnit5LogCollectorExtension
        implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestInstancePostProcessor {

    private List<JUnit5LogCollector> collectors;

    public JUnit5LogCollectorExtension() {
        System.out.println("Creating " + getClass().getSimpleName());
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        collectors = findCollectors(testInstance);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        collectors.stream().forEach(c -> c.beforeTest());

    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        collectors.stream().forEach(c -> c.afterTest());

    }

    private static List<JUnit5LogCollector> findCollectors(final Object testinstance) {
        List<Field> collectorFields = Arrays.stream(testinstance.getClass().getFields())
                .filter(field -> JUnit5LogCollector.class.isAssignableFrom(field.getType()))
                .collect(Collectors.toList());

        List<JUnit5LogCollector> collectors = new ArrayList<>(collectorFields.size());
        for (Field field : collectorFields) {
            try {
                collectors.add((JUnit5LogCollector) field.get(testinstance));
            } catch (IllegalAccessException e) {
                throw new ExtensionConfigurationException("Cannot access JUnit5LogCollector field " + field.getName() + " in " + field.getDeclaringClass());
            }
        }

        return unmodifiableList(collectors);
    }
}
