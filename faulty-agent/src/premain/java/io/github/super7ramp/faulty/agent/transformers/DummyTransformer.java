package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;

import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTranformer;
import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTransformers;
import io.github.super7ramp.faulty.agent.transformers.visitors.DummyVisitor;

/**
 * Dummy transformer.
 */
final class DummyTransformer extends AbstractTransformer {

	/** Rollback transformer. */
	private final RollbackTranformer rollbackTransformer;

	/**
	 * Constructor.
	 * 
	 * @param api                         the ASM API version
	 * @param transformableClassPredicate predicate to determine if a class shall be
	 *                                    transformed or excluded
	 */
	DummyTransformer(final int api, final Predicate<String> transformableClassPredicate) {
		super(api, transformableClassPredicate);
		rollbackTransformer = RollbackTransformers.defaultRollbackTransformer();
	}

	@Override
	protected final ClassVisitor classVisitor(final int api, final ClassVisitor delegate,
			final String visitedClassName) {
		return new DummyVisitor(api, delegate);
	}

	@Override
	protected final RollbackTranformer rollbackTransformer() {
		return rollbackTransformer;
	}

}
