package io.github.super7ramp.faulty.api;

/**
 * Raised when injection fails.
 */
public final class InjectionFailureException extends Exception {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message exception message
	 */
	public InjectionFailureException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public InjectionFailureException(final Throwable cause) {
		super(cause);
	}
}
