package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.InfiniteLoopMethodVisitor;

/**
 * Transformer to inject infinite loop.
 */
final class InfiniteLoopTransformer extends AbstractMethodInClassTransformer {

	/**
	 * Constructor.
	 *
	 * @param transformableClassPredicate
	 */
	InfiniteLoopTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> transformableMethodPredicate) {
		super(api, transformableClassPredicate, transformableMethodPredicate);
	}

	@Override
	final MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor) {
		return new InfiniteLoopMethodVisitor(api, delegateMethodVisitor);
	}

}
