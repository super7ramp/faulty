package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.MethodInClassVisitor;
import io.github.super7ramp.faulty.agent.transformers.visitors.MethodVisitorFactory;

/**
 * Base class for transformers modifying method codes.
 */
abstract class AbstractMethodInClassTransformer extends AbstractTransformer {

	/** Predicate to determine if method shall be transformed or excluded. */
	private final Predicate<String> transformableMethodPredicate;

	/**
	 * Constructor with custom {@link DefaultRollbackTransformer}
	 *
	 * @param api                           the ASM API version
	 * @param transformableClassPredicate   predicate to determine if a class shall
	 *                                      be transformed or excluded
	 * @param aTransformableMethodPredicate predicate to determine if method shall
	 *                                      be transformed or excluded
	 * @param rollbackTransformer           a custom
	 *                                      {@link DefaultRollbackTransformer}
	 */
	AbstractMethodInClassTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> aTransformableMethodPredicate) {
		super(api, transformableClassPredicate);
		transformableMethodPredicate = aTransformableMethodPredicate;
	}

	@Override
	protected final ClassVisitor classVisitor(final int api, final ClassVisitor delegateClassVisitor,
			final String visitedClassName) {
		final MethodVisitorFactory methodVisitorFactory = (delegateMethodVisitor,
				visitedMethodName) -> methodVisitor(api, delegateMethodVisitor, visitedClassName, visitedMethodName);
		return new MethodInClassVisitor(api, delegateClassVisitor, transformableMethodPredicate, methodVisitorFactory);
	}

	/**
	 * The method visitor the abstraction will call.
	 *
	 * @param api                   the ASM API version
	 * @param delegateMethodVisitor the delegate method visitor to delegate method
	 *                              calls to
	 * @param visitedClassName      name of the visited class
	 * @param visitedMethodName     name of the visited method
	 * @return the visitor
	 */
	protected abstract MethodVisitor methodVisitor(final int api, final MethodVisitor delegateMethodVisitor,
			final String visitedClassName, final String visitedMethodName);

}
