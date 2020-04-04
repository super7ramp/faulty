package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;

import io.github.super7ramp.faulty.agent.transformers.rollback.RollbackTransformer;
import io.github.super7ramp.faulty.agent.transformers.visitors.DummyVisitor;

/**
 * Dummy transformer.
 */
final class DummyTransformer extends AbstractTransformer {

	/** Rollback transformer. */
	private final RollbackTransformer rollbackTransformer;

	/**
	 * Constructor.
	 * 
	 * @param api                         the ASM API version
	 * @param transformableClassPredicate predicate to determine if a class shall be
	 *                                    transformed or excluded
	 */
	DummyTransformer(final int api, final Predicate<String> transformableClassPredicate) {
		super(api, transformableClassPredicate);
		rollbackTransformer = new RollbackTransformer();
	}

	@Override
	protected final ClassVisitor classVisitor(final int api, final ClassVisitor delegate,
			final String visitedClassName) {
		return new DummyVisitor(api, delegate);
	}

	@Override
	protected final RollbackTransformer rollbackTransformer() {
		return rollbackTransformer;
	}

}
