faulty gradle plugin
--------------------

## Description

Integrate the faulty agent to your java code executed by gradle.

## How to use it

```gradle
plugins {
    id 'io.github.super7ramp.faulty-plugin'
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

## TODO

* publish
* support java application plugin 
* see if it's possible to actually test it