package io.github.super7ramp.faulty.api;

/**
 * Runtime exception injection parameters.
 */
public interface RuntimeExceptionParameters {

	/** @return name of the class to apply the infinite loop to. */
	String className();

	/** @return name of the method to apply the infinite loop to. */
	String methodName();

	/**
	 * Create an {@link RuntimeExceptionParameters} from given argument.
	 * 
	 * @param className  name of the class to apply the infinite loop to
	 * @param methodName name of the method to apply the infinite loop to
	 * @return the desired {@link RuntimeExceptionParameters}
	 */
	static RuntimeExceptionParameters of(final String className, final String methodName) {
		return new RuntimeExceptionParameters() {
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
