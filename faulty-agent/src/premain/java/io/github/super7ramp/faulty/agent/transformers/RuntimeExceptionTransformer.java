/**
 * 
 */
package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.RuntimeExceptionMethodVisitor;

/**
 * Transformer to inject runtime exception.
 */
final class RuntimeExceptionTransformer extends AbstractMethodInClassTransformer {

	/**
	 * Constructor.
	 * 
	 * @param transformableClassPredicate
	 * @param transformableMethodPredicate
	 */
	public RuntimeExceptionTransformer(final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(transformableClassPredicate, transformableMethodPredicate);
	}

	@Override
	final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor) {
		return new RuntimeExceptionMethodVisitor(api, delegateMethodVisitor);
	}

}
