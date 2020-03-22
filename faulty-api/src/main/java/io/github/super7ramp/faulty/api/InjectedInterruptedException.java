package io.github.super7ramp.faulty.api;

/**
 * A {@link InterruptedException} that can be injected by agent on demand.
 */
public final class InjectedInterruptedException extends InterruptedException {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Exception message. */
	private static final String MESSAGE = "Injected interrupted exception";

	/**
	 * Constructor.
	 */
	public InjectedInterruptedException() {
		super(MESSAGE);
	}

}
