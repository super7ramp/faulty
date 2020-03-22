package io.github.super7ramp.faulty;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Ignore;
import org.junit.Test;

import io.github.super7ramp.faulty.api.AgentNotFoundException;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyFacade;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InfiniteLoopParameters;
import io.github.super7ramp.faulty.api.InjectionFailureException;

/**
 * Test on {@link FaultyServices#injectInfiniteLoop} service.
 */
public class InfiniteLoopTest {

	/**
	 * This class is not supposed to produce an infinite loop. Let's change that.
	 */
	private static class InoffensiveTask implements Runnable {
		@Override
		public final void run() {
			// empty
		}
	}

	/**
	 * Test that an {@link InoffensiveTask} turns into a deadly one once an infinite
	 * loop is injected in it.
	 */
	@Test(expected = TimeoutException.class)
	public void loopInjection() throws AgentNotFoundException, AgentNotLaunchedException, InjectionFailureException,
			InterruptedException, ExecutionException, TimeoutException {

		final InfiniteLoopParameters parameters = InfiniteLoopParameters.of(InoffensiveTask.class.getName(), "run");
		FaultyFacade.getServices().injectInfiniteLoop(parameters);

		/* The injected infinite loop shall make Future.get hang for more than 5s. */
		Executors.newSingleThreadExecutor().submit(new InoffensiveTask()).get(5, TimeUnit.SECONDS);
	}

	@Ignore("interruptible loop not implemented yet")
	@Test(expected = InterruptedException.class)
	public void interruptibleLoopInjection() {
		// TODO
	}

}
