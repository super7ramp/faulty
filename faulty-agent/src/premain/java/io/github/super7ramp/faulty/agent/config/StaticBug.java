package io.github.super7ramp.faulty.agent.config;

/**
 * Bug specified in agent configuration; will be permanent.
 */
public interface StaticBug {
	/**
	 * Bug kind.
	 */
	enum Kind {
		INFINITE_LOOP, INFINITE_INTERRUPTIBLE_LOOP, RUNTIME_EXCEPTION;
	}

	/**
	 * @return the class name
	 */
	String className();

	/**
	 * @return the method name
	 */
	String methodName();

	/**
	 * @return the kind of the bug
	 */
	Kind kind();
}