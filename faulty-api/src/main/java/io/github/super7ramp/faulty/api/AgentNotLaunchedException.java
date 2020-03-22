package io.github.super7ramp.faulty.api;

/**
 * Thrown when required agent is not running.
 */
public final class AgentNotLaunchedException extends Exception {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 *
	 * @param cause the cause
	 */
	public AgentNotLaunchedException(final Throwable cause) {
		super(cause);
	}

}
