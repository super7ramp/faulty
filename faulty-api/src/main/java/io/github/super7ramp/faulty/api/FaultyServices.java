package io.github.super7ramp.faulty.api;

/**
 * Faulty Services.
 */
public interface FaultyServices {

	/**
	 * Inject the specified infinite loop.
	 *
	 * @param parameters the infinite loop parameters
	 * @return the {@link RevertableBug} corresponding to the injected infinite loop
	 *         bug
	 * @throws AgentNotLaunchedException if injection failed because agent is not
	 *                                   launched
	 * @throws InjectionFailureException if injection failed because of another
	 *                                   reason
	 */
	RevertableBug injectInfiniteLoop(final InfiniteLoopParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException;

	/**
	 * Inject a {@link InjectedRuntimeException}.
	 *
	 * @param parameters the infinite loop parameters
	 * @return the {@link RevertableBug} corresponding to the injected runtime
	 *         exception bug
	 * @throws AgentNotLaunchedException if injection failed because agent is not
	 *                                   launched
	 * @throws InjectionFailureException if injection failed because of another
	 *                                   reason
	 */
	RevertableBug injectRuntimeException(final RuntimeExceptionParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException;

}
