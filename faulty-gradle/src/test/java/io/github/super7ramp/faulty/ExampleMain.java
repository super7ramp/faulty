package io.github.super7ramp.faulty;

import io.github.super7ramp.faulty.api.AgentNotFoundException;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyFacade;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * To be launched in a JavaExec task to check whether faulty gradle plugin is
 * able to run it with the faulty agent.
 */
public final class ExampleMain {

	/**
	 * @param args program args
	 * @throws IllegalStateException if agent is not found/not launched
	 */
	public static void main(final String[] args) {
		try {
			final RuntimeExceptionParameters parameters = RuntimeExceptionParameters.of("Toto", "tata");
			FaultyFacade.getServices().injectRuntimeException(parameters);
		} catch (final AgentNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (final AgentNotLaunchedException e) {
			throw new IllegalStateException(e);
		} catch (final InjectionFailureException e) {
			// it's normal, class doesn't exist
		}
	}

}
