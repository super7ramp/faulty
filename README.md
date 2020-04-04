faulty: Fantastic Agent Utility Leveraging Total Yoloness
---------------------------------------------------------

**faulty** is a [Java instrumentation agent](https://docs.oracle.com/en/java/javase/14/docs/api/java.instrument/java/lang/instrument/package-summary.html) which allows to inject faults in Java applications.

## Motivations

Provide a way to test resilience of a complex application:
- At application boundaries
- By injecting bugs in specific classes/packages, in particular in reportedly bad-quality/legacy/problem-nest/third-party code.

## Get started

### Inject bugs statically

E.g. for a demonstration.

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

### Inject bugs dynamically

E.g. for automated high level tests.

It supposes you have already some kind of framework to perform your high-level tests.

1. Retrieve the `faulty-api` and `faulty-agent` jars from XXX or build them with:

```
gradle faulty-api:jar faulty-agent:jar
```

2. Put them in the classpath of your test configuration, as wall as the `-javaagent:faulty-agent.jar` JVM argument. Alternatively, if you use gradle, you can let the [faulty gradle plugin](faulty-gradle/README.md) do the job

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
    final RevertableBug injectedLoop = faulty.injectInfiniteLoop(parameters);

    /*
     * Trigger TaskA.run() using whatever your application test framework provides. 
     */

    /*
     * Check your application manages the looping task.
     */
     
    /*
     * Optionally, eject the bug; might be useful if you don't restart the application
     * after every scenarios.
     */
     injectedLoop.revert();

}
```

## Known issues

Injecting bug dynamically may fail with this error:

```
java.lang.UnsupportedOperationException: class redefinition failed: attempted to change the schema (add/remove fields)
```

This is not supposed to happen: faulty only does very limited modifications, nothing supposed to utterly change class definition, in particular no class field added nor removed.

Maybe ASM produces a byte-code too different from what the compiler produces and thus from what the JVM initially read - this issue only occurs when the class has already been loaded by the JVM - preventing the JVM to do the re-transformation.

Or most probably there is something I don't understand, as usual.

Anyway, a workaround is to "pre-transform" the classes statically with the `preTransform` parameter:

```
java -jar myApp.jar -javaagent:faulty-agent.jar=\
    preTransform=com.example.myapp.ClassA,\
    preTransform=com.example.myapp.ClassB
```

It will just pass the class into the ASM default transformer and seems to work the issue around.
You have to mention precisely the classes that may be transformed later dynamically (no package name allowed).

## TODO

### Features

* More bugs. Ideas: Slowdown, Null result. Difficulty: Easy/Medium.
* Provide a way to let user create new bugs/extend existing ones. Difficulty: Hard.
* Create a gradle plugin to allow magic configuration of the agent (like jacoco agent): In progress. Difficulty: Harder than I thought.
* Make dependency on ASM utils optional (it's just for debug printing).

### Correctness

* Make really unique the identification of modified methods: 2 methods can have same name, include method descriptor in bug parameters.
* There are probably issues linked to rollback transformation: E.g. when two bugs applied at the same location, rollback may not work as expected. Solution may be to just reject bug injection for method that has already a bug (=> create real bug memory).
* Check retransformation issues: Why is preTransform needed?
* API version passed are parameter is not actually use, to fix.
* Check if services work remotely: App launched in a JVM with agent, test framework in another JVM, attach to the app JVM, call faulty services.