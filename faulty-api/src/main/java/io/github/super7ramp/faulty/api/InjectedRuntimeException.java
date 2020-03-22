package io.github.super7ramp.faulty.api;

/**
 * A {@link RuntimeException} that can be injected by agent on demand.
 */
public final class InjectedRuntimeException extends RuntimeException {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Exception message. */
	private static final String MESSAGE = "Injected runtime exception";

	/**
	 * Constructor.
	 */
	public InjectedRuntimeException() {
		super(MESSAGE);
	}

}
