package io.github.super7ramp.faulty.api;

/**
 * An access to an injected bug.
 */
public interface RevertableBug {

	/**
	 * Remove the injected bug, i.e. restore the original code.
	 * <p>
	 * Calling this more than once is nonsense but it will not fail.
	 */
	void revert() throws AgentNotLaunchedException, InjectionFailureException;

}
