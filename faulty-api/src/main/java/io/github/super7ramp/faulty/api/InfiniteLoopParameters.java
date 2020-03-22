package io.github.super7ramp.faulty.api;

/**
 * 
 */
public interface InfiniteLoopParameters {

	/** Class name to apply infinite loop to. */
	String className();

	/** Method name. */
	String methodName();

//	public final boolean interruptible();

	static InfiniteLoopParameters of(final String className, final String methodName) {
		return new InfiniteLoopParameters() {
			@Override
			public String methodName() {
				return methodName;
			}

			@Override
			public String className() {
				return className;
			}
		};
	}
}
