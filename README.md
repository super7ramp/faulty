faulty: A Java agent which inserts faults
-----------------------------------------

**faulty** is an experimental light [ASM](https://asm.ow2.io/)-based [instrumentation agent](https://docs.oracle.com/en/java/javase/14/docs/api/java.instrument/java/lang/instrument/package-summary.html) which allows to inject faults in Java application classes.

## Motivations

As often, for bad reasons:
* Test response of a complex application in face of bugs occurring in specific classes/packages, in particular in reportedly bad-quality/legacy/problem-nest/third-party code.
* Test this behavior at system level - meaning at app boundaries - instead of application level - app components boundaries

In other words, it allows to perform verifications for critical situations normally impossible to trigger at a high black-box level; So more for reassuring people than for rationale reason…

## Get started

### Inject bugs statically for a demonstration

1. Retrieve the agent jar from XXX or build it with:

```
gradle faulty-agent:jar
```

2. Put it in your classpath.

3. Launch your app with:

```
java -jar myApp.jar -javaagent:faulty-agent.jar=\
    infiniteLoop=com.example.myapp.ClassA.method,\
    infiniteLoop=com.example.myapp.ClassB.method,\
    infiniteInterruptibleLoop=com.example.myapp.ClassC.method,\
    runtimeException=com.example.myapp.ClassD.method
```

See XXX for the the list of injectable bugs.

### Inject bugs dynamically for automated high level tests

It supposes you have already some kind of framework to do perform your high-level tests.

1. Retrieve the `faulty-api` and `faulty-agent` jars from XXX or build them with:

```
gradle faulty-api:jar faulty-agent:jar
```

2. Put them in the classpath of your test configuration, as wall as the `-javaagent:faulty-agent.jar` JVM argument.

3. Test it.

```java
@Test
public void test() {
   
    /*
     * Launch your app with using whatever your application test framework provides.  
     */

    /*
     * Call the faulty services and inject an interruptible loop.
     */
    final FaultyServices faulty = FaultyFacade.getServices();
    final InfiniteLoopParameters parameters = InfiniteLoopParameters.of("com.example.myapp.TaskA",
                                                                        "run",
                                                                        true /* interruptible. */);
    faulty.injectInfiniteLoop(parameters);

    /*
     * Trigger TaskA.run() using whatever your application test framework provides. 
     */

    /*
     * Check your application manages the looping task.
     */

}

```

## TODO

* More bugs (yes).
* Check for retransformation issues.
* Check if services work remotely: App launched in a JVM with agent, test framework in another JVM, attach to the app JVM, call faulty services.