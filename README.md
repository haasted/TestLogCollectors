# Log Collectors

[![Build Status](https://travis-ci.org/haasted/TestLogCollectors.svg?branch=master)](https://travis-ci.org/haasted/TestLogCollectors)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dk.bitcraft/LogCollector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dk.bitcraft/LogCollector)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/haasted/TestLogCollectors/blob/master/LICENSE)
[![Javadoc](https://img.shields.io/badge/javadoc-OK-blue.svg)](https://javadoc.io/doc/dk.bitcraft/LogCollector/)

Application logging is important and occasionally important enough to make it worth creating automated tests to verify it. Enter Log Collectors, which captures log records for inspection or verification during testing. 

The framework hooks into any logger it is provided and intercepts its messages, making it possible to inspect or verify them.

It is applicable in situations where you want to
 - ensure that THE warning is emitted during an error 
 - verify that the audit logs gets populated correctly

Log Collectors currently has support for the following libraries
 - [Log4j2](https://logging.apache.org/log4j/2.x/)
 - [Logback](https://logback.qos.ch/)
 - [java.util.logging](https://docs.oracle.com/javase/8/docs/technotes/guides/logging/overview.html)
 - [slf4j](https://www.slf4j.org/) (slf4j-simple, logback, log4j, java.util.logging)

It integrates directly with these testing frameworks:
 - [JUnit 4](http://junit.org/junit4/) through a [rule](https://github.com/junit-team/junit4/wiki/Rules).
 - [TestNG](http://testng.org/) through a [listener](http://testng.org/doc/documentation-main.html#testng-listeners)

The goals of the framework are
 - No forced dependencies.
 - Support for the most widespread logging frameworks

Feedback is appreciated. If you find the framework useful, or have suggestions for improving it, get in touch!

# Usage

The following examples show how to add the framework to a test, provide it with the logger and inspect the log entries generated by testing a component.

### JUnit
```java
Logger logger = LogManager.getLogger("acme.Gizmo");

@Rule
public JUnit4LogCollector collector = new JUnit4LogCollector(logger);

@Test
public void testGizmo() {
    Gizmo gizmo = new Gizmo();
    gizmo.run();

    List<LogEvent> rawLogs = (List<LogEvent>) collector.getRawLogs();
    assertTrue(rawLogs.stream().noneMatch(l -> l.getLevel() == Level.ERROR));
}
```

### TestNG
```java
@Test
@Listeners(TestNGLogCollector.class)
public class GizmoTest {

    final static Logger logger = LoggerFactory.getLogger(SLF4JTest.class);

    @BeforeTest
    public void captureLogger() {
        TestNGLogCollector.setLogSource(logger);
    }

    public void testGizmo() {
        Gizmo gizmo = new Gizmo();
        gizmo.run();

        List<LogEvent> rawLogs = (List<LogEvent>) TestNGLogCollector.getRawLogs();
        assertTrue(rawLogs.stream().noneMatch(l -> l.getLevel() == Level.ERROR));
    }
}
```


# Installation

Log Collectors is available from [Maven Central](https://maven-badges.herokuapp.com/maven-central/dk.bitcraft/LogCollector).

Maven installation
```xml
<dependency>
    <groupId>dk.bitcraft</groupId>
    <artifactId>LogCollector</artifactId>
    <version>0.8.0</version>
    <scope>test</scope>
</dependency>
```

Gradle installation
```
testCompile 'dk.bitcraft:LogCollector:0.8.0'
```

# Future work

  - Support for JUnit 5.
  - Come up with a better name for the framework.
  - Improve Javadoc and documentation.
  - Discover the intricacies of the various logging frameworks in real-world settings and adapt to them.
  - Avoid the ugly cast in `getRawLogs`.
  - Detect SLF4j's `NOPLogger` and replace it with `SimpleLogger`
  - Any ideas? Get in touch!
