package io.github.super7ramp.faulty.api;

/**
 * Infinite loop parameters.
 */
public interface InfiniteLoopParameters {

	/** @return name of the class to apply the infinite loop to. */
	String className();

	/** @return name of the method to apply the infinite loop to. */
	String methodName();

	/** @return whether a thread interruption will break the injected loop. */
	boolean interruptible();

	/**
	 * Create an {@link InfiniteLoopParameters} from given argument.
	 * 
	 * @param className       name of the class to apply the infinite loop to
	 * @param methodName      ame of the method to apply the infinite loop to
	 * @param isInterruptible whether a thread interruption will break the injected
	 *                        loop
	 * @return the desired {@link InfiniteLoopParameters}
	 */
	static InfiniteLoopParameters of(final String className, final String methodName, final boolean isInterruptible) {
		return new InfiniteLoopParameters() {
			@Override
			public String methodName() {
				return methodName;
			}

			@Override
			public String className() {
				return className;
			}

			@Override
			public boolean interruptible() {
				return isInterruptible;
			}
		};
	}
}
