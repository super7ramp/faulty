package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.rollback.InfiniteLoopRollbackTransformer;
import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTransformer;
import io.github.super7ramp.faulty.agent.transformers.visitors.InfiniteLoopMethodVisitor;

/**
 * Transformer to inject infinite loop.
 */
final class InfiniteLoopTransformer extends AbstractMethodInClassTransformer {

	/** Rollback transformer. */
	private final InfiniteLoopRollbackTransformer rollbackTransformer;

	/**
	 * Constructor.
	 * 
	 * @param api                          the ASM API version
	 * @param transformableClassPredicate  predicate to determine if a class shall
	 *                                     be transformed or excluded
	 * @param transformableMethodPredicate predicate to determine if method shall be
	 *                                     transformed or excluded
	 */
	InfiniteLoopTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(api, transformableClassPredicate, transformableMethodPredicate);
		rollbackTransformer = new InfiniteLoopRollbackTransformer();
	}

	@Override
	protected final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor,
			final String visitedClassName, final String visitedMethodName) {
		return new InfiniteLoopMethodVisitor(api, delegateMethodVisitor, visitedClassName, visitedMethodName)
				.addListener(rollbackTransformer);
	}

	@Override
	protected final RollbackTransformer rollbackTransformer() {
		return rollbackTransformer;
	}

}
