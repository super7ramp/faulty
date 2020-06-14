## faulty

**faulty** is a [Java instrumentation agent](https://docs.oracle.com/en/java/javase/14/docs/api/java.instrument/java/lang/instrument/package-summary.html) which allows to inject faults in Java applications.

_This project is a first experiment with Java agents and the ASM library, it'll hardly be useful to anyone other than its author._

### Inject fault statically

```
java -jar myApp.jar \
     -javaagent:/path/to/faulty-agent.jar=\
        infiniteLoop=com.example.myapp.ClassA.method,\
        infiniteLoop=com.example.myapp.ClassB.method,\
        infiniteInterruptibleLoop=com.example.myapp.ClassC.method,\
        runtimeException=com.example.myapp.ClassD.method
```

No control on method signature, if two methods have the same name then both will be instrumented.

### Inject faults dynamically

```java
/*
 * Get the faulty services.
 */
final FaultyServices faulty = Faulty.getServices();

/*
 * Inject an interruptible loop.
 */
final InfiniteLoopParameters parameters = InfiniteLoopParameters.of("com.example.myapp.TaskA",
                                                                    "run",
                                                                    true /* interruptible. */);
final RevertableTransformation injectedLoop = faulty.injectInfiniteLoop(parameters);
 
/*
 * Optionally, eject the bug; might be useful if you don't restart the application after every
 * scenarios.
 */
injectedLoop.revert();
```

As only the `-javaagent` / `premain()` way is supported to launch the agent, the above code must be launched inside the same JVM e.g. launched within a JUnit test together with the application.

If one wishes to use a different JVM, the way to go is to code an `agentmain()` method inside faulty and use the [Attach API](https://docs.oracle.com/en/java/javase/14/docs/api/jdk.attach/com/sun/tools/attach/VirtualMachine.html) in order to remotely launch the agent.

### Known issues

Injecting bug dynamically may fail with this error:

```
java.lang.UnsupportedOperationException: class redefinition failed: attempted to change the schema (add/remove fields)
```

This is not supposed to happen: faulty is not supposed to change class definition, it just modifies method bodies, so no class field added nor removed.

Maybe ASM produces a byte-code which is too different from what the compiler produces and thus from what the JVM initially read - this issue only occurs when the class has already been loaded by the JVM - preventing the JVM to do the re-transformation.

Anyway, a workaround is to "pre-transform" the classes statically with the `preTransform` parameter:

```
java -jar myApp.jar -javaagent:/path/to/faulty-agent.jar=\
    preTransform=com.example.myapp.ClassA,\
    preTransform=com.example.myapp.ClassB
```

It will just pass the class into the ASM default transformer and seems to work around the issue.