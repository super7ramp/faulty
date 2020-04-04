package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTransformer;
import io.github.super7ramp.faulty.agent.transformers.visitors.RuntimeExceptionMethodVisitor;

/**
 * Transformer to inject runtime exception.
 */
final class RuntimeExceptionTransformer extends AbstractMethodInClassTransformer {

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
	RuntimeExceptionTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(api, transformableClassPredicate, transformableMethodPredicate);
		rollbackTransformer = new RollbackTransformer();
	}

	@Override
	protected final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor,
			final String visitedClassName, final String visitedMethodName) {
		return new RuntimeExceptionMethodVisitor(api, delegateMethodVisitor);
	}

	@Override
	protected final RollbackTransformer rollbackTransformer() {
		return rollbackTransformer;
	}

}
