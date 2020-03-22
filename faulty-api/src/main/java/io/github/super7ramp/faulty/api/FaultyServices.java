package io.github.super7ramp.faulty.api;

/**
 * Faulty Services.
 */
public interface FaultyServices {

	/**
	 * Inject the specified infinite loop.
	 *
	 * @param parameters the infinite loop parameters
	 * @throws AgentNotLaunchedException if injection failed because agent is not
	 *                                   launched
	 * @throws InjectionFailureException if injection failed because of another
	 *                                   reason
	 */
	void injectInfiniteLoop(final InfiniteLoopParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException;

	/**
	 * Inject a {@link InjectedRuntimeException}.
	 *
	 * @param parameters the infinite loop parameters
	 * @throws AgentNotLaunchedException if injection failed because agent is not
	 *                                   launched
	 * @throws InjectionFailureException if injection failed because of another
	 *                                   reason
	 */
	void injectRuntimeException(final RuntimeExceptionParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException;

}
