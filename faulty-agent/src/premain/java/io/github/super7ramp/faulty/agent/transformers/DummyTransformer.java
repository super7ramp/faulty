package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.DummyVisitor;

/**
 * Dummy transformer.
 */
final class DummyTransformer extends AbstractTransformer {

	/**
	 * Constructor.
	 *
	 * @param transformableClassPredicate predicate to determine if a class shall be
	 *                                    transformed or excluded
	 */
	DummyTransformer(final Predicate<String> transformableClassPredicate) {
		super(transformableClassPredicate);
	}

	@Override
	protected final ClassVisitor classVisitor(final int api, final ClassVisitor delegate) {
		return new DummyVisitor(api, delegate);
	}

}
