package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.InfiniteLoopMethodVisitor;

/**
 * Transformer to inject infinite loop.
 */
final class InfiniteLoopTransformer extends AbstractMethodInClassTransformer {

	/**
	 * A custom {@link RollbackTransformer} that will un-block the infinite loop as
	 * {@link #preTransform()} step.
	 * <p>
	 * If this step is not performed then infinite loop will keep going since that,
	 * upon transformation, active frames continue to run the bytecode of the
	 * original method (i.e. here the code with an infinite loop): Only new
	 * invocation would be loop-free.
	 *
	 * @see java.lang.instrument.Instrumentation#retransformClasses(Class...)
	 *      Instrumentation#retransformClasses(Class...)
	 */
	private static class InfiniteLoopRollbackTransformer extends RollbackTransformer {

		/**
		 * Constructor.
		 */
		InfiniteLoopRollbackTransformer() {
			// Nothing to do.
		}

		@Override
		protected void preTransform() {
			// TODO !
		}
	}

	/**
	 * Constructor.
	 *
	 * @param transformableClassPredicate
	 */
	InfiniteLoopTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(api, transformableClassPredicate, transformableMethodPredicate, new InfiniteLoopRollbackTransformer());
	}

	@Override
	final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor) {
		return new InfiniteLoopMethodVisitor(api, delegateMethodVisitor);
	}

}
