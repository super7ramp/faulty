package io.github.super7ramp.faulty;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import io.github.super7ramp.faulty.api.AgentNotFoundException;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyFacade;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InfiniteLoopParameters;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RevertableBug;

/**
 * Test on {@link FaultyServices#injectInfiniteLoop} service.
 */
public class InfiniteLoopTest {

	/** Services under tests. */
	private FaultyServices services;

	/**
	 * Initialize services under test.
	 *
	 * @throws AgentNotFoundException should not happen
	 */
	@Before
	public void before() throws AgentNotFoundException {
		services = FaultyFacade.getServices();
	}

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
	 * This class is not supposed to produce an infinite loop. Let's change that.
	 */
	private static class InoffensiveInterruptibleTask implements Runnable {
		@Override
		public final void run() {
			try {
				new Nothing().run();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * This class is not supposed to produce an infinite loop. Let's change that.
	 */
	private static class Nothing {
		public final void run() throws InterruptedException {
			// Nothing.
		}
	}

	/**
	 * Test that an {@link InoffensiveTask} can loop one once an infinite loop is
	 * injected in it.
	 * 
	 * @throws AgentNotLaunchedException should not happen
	 * @throws InjectionFailureException should not happen
	 * @throws InterruptedException      should not happen
	 * @throws ExecutionException        should not happen
	 * @throws TimeoutException          should not happen
	 */
	@Test(expected = TimeoutException.class)
	public void loopInjection() throws AgentNotLaunchedException, InjectionFailureException, InterruptedException,
			ExecutionException, TimeoutException {

		final InfiniteLoopParameters parameters = InfiniteLoopParameters.of(InoffensiveTask.class.getName(), "run",
				false);
		services.injectInfiniteLoop(parameters);

		/* The injected infinite loop shall make Future.get hang for more than 5s. */
		Executors.newSingleThreadExecutor().submit(new InoffensiveTask()).get(5, TimeUnit.SECONDS);
	}

	/**
	 * Test that loop bug can be reverted.
	 * 
	 * @throws AgentNotLaunchedException should not happen
	 * @throws InjectionFailureException should not happen
	 * @throws InterruptedException      should not happen
	 * @throws ExecutionException        should not happen
	 */
	@Test
	public void revertLoopInjection()
			throws AgentNotLaunchedException, InjectionFailureException, InterruptedException, ExecutionException {

		final InfiniteLoopParameters parameters = InfiniteLoopParameters.of(InoffensiveTask.class.getName(), "run",
				false);
		final RevertableBug injectedBug = services.injectInfiniteLoop(parameters);

		/* The injected infinite loop shall make Future.get hang for more than 5s. */
		final Future<?> taskCompletion = Executors.newSingleThreadExecutor().submit(new InoffensiveTask());
		try {
			taskCompletion.get(5, TimeUnit.SECONDS);
		} catch (final TimeoutException e) {
			// continue
		}

		injectedBug.revert();

		try {
			taskCompletion.get(5, TimeUnit.SECONDS);
		} catch (final TimeoutException e) {
			fail("Infinite loop obviously not reverted.");
		}
	}

	/**
	 * Test that an {@link InoffensiveInterruptibleTask} can loop one once an
	 * infinite loop is injected in it.
	 * 
	 * @throws AgentNotLaunchedException should not happen
	 * @throws InjectionFailureException should not happen
	 * @throws InterruptedException      should not happen
	 */
	@Test
	public void interruptibleLoopInjection()
			throws AgentNotLaunchedException, InjectionFailureException, InterruptedException {

		final InfiniteLoopParameters parameters = InfiniteLoopParameters.of(Nothing.class.getName(), "run", true);
		services.injectInfiniteLoop(parameters);

		/* The injected infinite loop shall make task hangs for more than 5s. */
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new InoffensiveInterruptibleTask());
		executor.shutdown();
		assertFalse(executor.awaitTermination(5, TimeUnit.SECONDS));

		/*
		 * Check that injected loop checks for thread state:
		 * InjectedInterruptedException will be thrown.
		 */
		executor.shutdownNow();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

}
