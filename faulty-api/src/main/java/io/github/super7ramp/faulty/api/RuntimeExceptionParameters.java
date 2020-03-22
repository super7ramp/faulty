package io.github.super7ramp.faulty.api;

/**
 * Runtime exception injection parameters.
 */
public interface RuntimeExceptionParameters {

	/** Class name to inject exception into. */
	String className();

	/** Method name to inject exception into. */
	String methodName();

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
