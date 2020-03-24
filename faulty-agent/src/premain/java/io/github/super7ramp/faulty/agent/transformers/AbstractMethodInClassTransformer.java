package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.MethodInClassVisitor;

/**
 * Base class for transformers modifying method codes.
 */
abstract class AbstractMethodInClassTransformer extends AbstractTransformer {

	private final Predicate<String> transformableMethodPredicate;

	public AbstractMethodInClassTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> aTransformableMethodPredicate) {
		super(api, transformableClassPredicate);
		transformableMethodPredicate = aTransformableMethodPredicate;
	}

	@Override
	protected final ClassVisitor classVisitor(final int api, final ClassVisitor delegateClassVisitor) {
		return new MethodInClassVisitor(api, delegateClassVisitor, transformableMethodPredicate,
				(delegateMethodVisitor) -> methodVisitor(api, delegateMethodVisitor));
	}

	/**
	 * The method visitor the abstraction will call.
	 *
	 * @param api                   the ASM API version
	 * @param delegateMethodVisitor the delegate method visitor to delegate method
	 *                              calls to
	 * @return the visitor
	 */
	abstract MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor);

}
