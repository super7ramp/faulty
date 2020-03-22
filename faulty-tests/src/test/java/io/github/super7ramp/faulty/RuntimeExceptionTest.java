package io.github.super7ramp.faulty;

import org.junit.Test;

import io.github.super7ramp.faulty.api.AgentNotFoundException;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyFacade;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InjectedRuntimeException;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * Tests on {@link FaultyServices#injectRuntimeException}.
 */
public class RuntimeExceptionTest {

	/**
	 * This class is not supposed to produce an infinite loop. Let's change that.
	 */
	private static class InoffensiveComputer {
		public final void compute() {
			// empty
		}
	}

	@Test(expected = InjectedRuntimeException.class)
	public void runtimeException() throws AgentNotLaunchedException, InjectionFailureException, AgentNotFoundException {

		final RuntimeExceptionParameters parameters = RuntimeExceptionParameters.of(InoffensiveComputer.class.getName(),
				"compute");
		FaultyFacade.getServices().injectRuntimeException(parameters);

		new InoffensiveComputer().compute();
	}
}
