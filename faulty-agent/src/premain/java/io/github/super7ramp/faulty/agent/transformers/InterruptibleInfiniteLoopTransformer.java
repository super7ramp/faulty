package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.InterruptibleInfiniteLoopMethodVisitor;

/**
 * Transformer to inject interruptible infinite loop.
 * <p>
 * TODO validate signature of the method first, it must throw an
 * InterruptedException otherwise transformation won't work
 */
final class InterruptibleInfiniteLoopTransformer extends AbstractMethodInClassTransformer {

	/**
	 * Constructor.
	 *
	 * @param transformableClassPredicate
	 */
	InterruptibleInfiniteLoopTransformer(final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(transformableClassPredicate, transformableMethodPredicate);
	}

	@Override
	final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor) {
		return new InterruptibleInfiniteLoopMethodVisitor(api, delegateMethodVisitor);
	}

}
