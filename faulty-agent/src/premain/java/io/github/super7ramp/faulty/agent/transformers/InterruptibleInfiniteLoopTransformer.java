package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTransformer;
import io.github.super7ramp.faulty.agent.transformers.visitors.InterruptibleInfiniteLoopMethodVisitor;

/**
 * Transformer to inject interruptible infinite loop.
 */
/*
 * TODO validate signature of the method first, it must throw an
 * InterruptedException otherwise transformation won't work
 *
 * TODO use InfiniteLoopRollbackTransformer
 */
final class InterruptibleInfiniteLoopTransformer extends AbstractMethodInClassTransformer {

	/** Rollback transformer. */
	private final RollbackTransformer rollbackTransformer;

	/**
	 * Constructor.
	 * 
	 * @param api                          the ASM API version
	 * @param transformableClassPredicate  predicate to determine if a class shall
	 *                                     be transformed or excluded
	 * @param transformableMethodPredicate predicate to determine if method shall be
	 *                                     transformed or excluded
	 */
	InterruptibleInfiniteLoopTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(api, transformableClassPredicate, transformableMethodPredicate);
		rollbackTransformer = new RollbackTransformer();
	}

	@Override
	protected final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor,
			final String visitedClassName, final String visitedMethodName) {
		return new InterruptibleInfiniteLoopMethodVisitor(api, delegateMethodVisitor);
	}

	@Override
	protected final RollbackTransformer rollbackTransformer() {
		return rollbackTransformer;
	}

}
