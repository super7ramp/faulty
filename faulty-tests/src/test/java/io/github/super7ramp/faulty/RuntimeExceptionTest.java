package io.github.super7ramp.faulty;

import static org.junit.Assert.fail;

import org.junit.Test;

import io.github.super7ramp.faulty.api.AgentNotFoundException;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyFacade;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InjectedRuntimeException;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RevertableTransformation;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * Tests on {@link FaultyServices#injectRuntimeException}.
 */
public class RuntimeExceptionTest {

	/**
	 * This class is not supposed to produce a runtime exception. Let's change that.
	 */
	private static class InoffensiveComputer {
		public final void compute() {
			// empty
		}
	}

	/**
	 * This class is not supposed to produce a runtime exception. Let's change that.
	 */
	private static class AnotherInoffensiveComputer {
		public final void compute() {
			// empty
		}
	}

	/**
	 * Check that runtime exception injection works.
	 * 
	 * @throws AgentNotLaunchedException should not happen
	 * @throws InjectionFailureException should not happen
	 * @throws AgentNotFoundException    should not happen
	 */
	@Test(expected = InjectedRuntimeException.class)
	public void injectRuntimeException()
			throws AgentNotLaunchedException, InjectionFailureException, AgentNotFoundException {

		final RuntimeExceptionParameters parameters = RuntimeExceptionParameters.of(InoffensiveComputer.class.getName(),
				"compute");
		FaultyFacade.getServices().injectRuntimeException(parameters);

		new InoffensiveComputer().compute();
	}

	/**
	 * Test that injected runtime exception cancellation works.
	 * 
	 * @throws AgentNotLaunchedException should not happen
	 * @throws InjectionFailureException should not happen
	 * @throws AgentNotFoundException    should not happen
	 */
	@Test
	public void ejectRuntimeException()
			throws AgentNotLaunchedException, InjectionFailureException, AgentNotFoundException {

		final RuntimeExceptionParameters parameters = RuntimeExceptionParameters.of(InoffensiveComputer.class.getName(),
				"compute");
		final RevertableTransformation injectedBug = FaultyFacade.getServices().injectRuntimeException(parameters);

		final InoffensiveComputer computer = new InoffensiveComputer();
		try {
			computer.compute();
			fail("Should have thrown an exception");
		} catch (final InjectedRuntimeException e) {
			// perfect
		}

		// let's revert the bug now
		injectedBug.revert();

		// shall not raise an exception
		computer.compute();
	}

	/**
	 * Check that runtime exception static injection works.
	 * <p>
	 * See gradle test run configuration.
	 * 
	 */
	@Test(expected = InjectedRuntimeException.class)
	public void staticIjectRuntimeException() {
		new AnotherInoffensiveComputer().compute();
	}
}
