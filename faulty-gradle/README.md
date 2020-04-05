## faulty gradle plugin

### Description

Integrate the faulty agent to your java code executed by gradle. **Work in progress.**

### How to use it

```gradle
plugins {
    id 'io.github.super7ramp.faulty-plugin' version: '1.0-snapshot'
}

dependencies {
    // If you do dynamic injection with the faulty-api in your tests 
    testImplementation faultyApi()
}

test {

    /*
     * The faulty task extension can be added to any JavaExec task.
     * Here's an example on a test task.
     */
    faulty {
        /*
         * Enable faulty agent. This is mandatory.
         */
        enabled = true

        /*
         * Inject these bugs at agent startup.
         * Bug can still be injected at runtime if tests uses
         * the faulty-api.
         */ 
        staticBugs = [
            runtimeException: [
                'com.example.myapp.ClassD.method'
            ]
        ]

        /*
         * Pre-transform these classes at agent startup because
         * they are subject to dynamic injection issues.
         */
        preTransform = [
            'com.example.myapp.ClassA',
            'com.example.myapp.ClassB'
        ]
    }
}
```

### TODO

* Add faultyApi() task(?) which returns the faulty-api dependency
* Support java application plugin
* Actually test it