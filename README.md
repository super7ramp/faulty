faulty: Fantastic Agent Utility Leveraging Total Yoloness
---------------------------------------------------------

**faulty** is a [Java instrumentation agent](https://docs.oracle.com/en/java/javase/14/docs/api/java.instrument/java/lang/instrument/package-summary.html) which allows to inject faults in Java applications.

## Motivations

Provide a way to test resilience of a running complex application by injecting bugs in specific classes/packages, in particular in reportedly bad-quality/legacy/problem-nest/third-party code.

## Get started

### Inject bugs statically

Launch your app `myApp.jar` with `faulty-agent.jar` as agent:

```
java -jar myApp.jar \
     -javaagent:/path/to/faulty-agent.jar=\
        infiniteLoop=com.example.myapp.ClassA.method,\
        infiniteLoop=com.example.myapp.ClassB.method,\
        infiniteInterruptibleLoop=com.example.myapp.ClassC.method,\
        runtimeException=com.example.myapp.ClassD.method
```

ASM and ASM util must be added in class path as well (or bundled in agent jar).

### Inject bugs dynamically

E.g. for automated high level tests, assuming you have already some kind of framework to perform your high-level tests.

1. Put `faulty-api` in the classpath of your test configuration and add `-javaagent:/path/to/faulty-agent.jar` to the JVM arguments. Example with gradle:

```gradle
configuration faultyAgent {
    implementation 'io.github.super7ramp.faulty:faulty-agent'
}

test {
    testImplementation 'io.github.super7ramp.faulty:faulty-api'
    testRuntimeOnly 'org.ow2.asm:asm'
    testRuntimeOnly 'org.ow2.asm:asm-util'

    def faultyAgentArgs = '' // optional, see above for arguments you can pass
    def faultyAgentJar = configurations.faultyAgent.asPath[0]
    jvmArgs '-javaagent:' + faultyAgentJar + '=' + args
}

```

Alternatively, you can let the [faulty gradle plugin](faulty-gradle/README.md) do the job for you.

2. Test it.

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

This is not supposed to happen: faulty is not supposed to change class definition, it just modifies method bodies, so no class field added nor removed…

Maybe ASM produces a byte-code too different from what the compiler produces and thus from what the JVM initially read - this issue only occurs when the class has already been loaded by the JVM - preventing the JVM to do the re-transformation.

Anyway, a workaround is to "pre-transform" the classes statically with the `preTransform` parameter:

```
java -jar myApp.jar -javaagent:/path/to/faulty-agent.jar=\
    preTransform=com.example.myapp.ClassA,\
    preTransform=com.example.myapp.ClassB
```

It will just pass the class into the ASM default transformer and seems to work the issue around.
You have to mention precisely the classes that may be transformed later dynamically (no package name allowed).

## TODO

### Features

* More bugs, ideas: Slowdown, Null result.
* Provide a way to let user create _basic_ new bugs/extend existing ones.
* Create a gradle plugin to allow magic configuration of the agent (like jacoco agent).
* Make dependency on ASM util optional (it's just for debug printing).

### Correctness

* Make really unique the identification of modified methods: 2 methods can have same name, include method descriptor in bug parameters.
* There are probably issues linked to rollback transformation: E.g. when two bugs applied at the same location, rollback may not work as expected. Solution may be to just reject bug injection for method that has already a bug (=> create real bug memory).
* Check retransformation issues: Why is preTransform needed?
* API version passed are parameter is not actually use, to fix.
* More tests.
* Check if services work remotely: App launched in a JVM with agent, test framework in another JVM, attach to the app JVM, call faulty services.