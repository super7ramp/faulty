package io.github.super7ramp.faulty;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
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
import io.github.super7ramp.faulty.api.RevertableTransformation;

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
	private static class AnotherInoffensiveTask implements Runnable {
		@Override
		public final void run() {
			// empty
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
	 * Test that an {@link AnotherInoffensiveTask} can loop one since an infinite
	 * loop is statically injected.
	 * <p>
	 * See gradle test run configuration.
	 * 
	 * @throws InterruptedException should not happen
	 * @throws ExecutionException   should not happen
	 * @throws TimeoutException     should not happen
	 */
	@Test(expected = TimeoutException.class)
	public void staticLoopInjection() throws InterruptedException, ExecutionException, TimeoutException {
		/* The injected infinite loop shall make Future.get hang for more than 5s. */
		Executors.newSingleThreadExecutor().submit(new AnotherInoffensiveTask()).get(5, TimeUnit.SECONDS);
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
		final RevertableTransformation injectedBug = services.injectInfiniteLoop(parameters);

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

}
