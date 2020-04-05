package io.github.super7ramp.faulty.agent.transformers.rollback;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A custom {@link DefaultRollbackTransformer} that will break the infinite loop
 * as {@link #preTransform()} step.
 * <p>
 * If this step is not performed then infinite loop will keep going since that
 * upon transformation active frames continue to run the bytecode of the
 * original method. Only new invocations would be loop-free.
 *
 * @see java.lang.instrument.Instrumentation#retransformClasses(Class...)
 *      Instrumentation#retransformClasses(Class...)
 */
final class InfiniteLoopRollbackTransformer extends DefaultRollbackTransformer implements ListeningRollbackTransformer {

	/**
	 * Loops injected by the {@link InfiniteLoopTransformer} associated to this
	 * {@link InfiniteLoopRollbackTransformer}.
	 */
	private final Collection<String> loops;

	/**
	 * Constructor.
	 */
	InfiniteLoopRollbackTransformer() {
		loops = new ArrayList<>();
	}

	@Override
	protected final void preTransform(final String className) {
		/*
		 * Here we just set to false the exit condition of the injected loop (see
		 * InfiniteLoopMethodVisitor). TODO is it possible to "just" drop the frame like
		 * a debugger instead? how does a debugger do that?
		 */
		loops.forEach(ActiveLoops::remove);

		/*
		 * Pre-transform should be called only one, but just in case.
		 */
		loops.clear();
	}

	@Override
	public final void notifyTransformation(final String className, final String methodName) {
		final String identifier = ActiveLoops.idOf(className, methodName);
		loops.add(identifier);
		ActiveLoops.add(identifier);
	}
}