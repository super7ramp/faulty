package io.github.super7ramp.faulty.api;

/**
 * Thrown when agent services are not found in classpath.
 */
public final class AgentNotFoundException extends Exception {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public AgentNotFoundException(final Throwable cause) {
		super(cause);
	}

}
