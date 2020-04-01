package io.github.super7ramp.faulty.agent.transformers;

import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import io.github.super7ramp.faulty.agent.transformers.visitors.MethodInClassVisitor;

/**
 * Base class for transformers modifying method codes.
 */
abstract class AbstractMethodInClassTransformer extends AbstractTransformer {

	/** Predicate to determine if method shall be transformed or excluded. */
	private final Predicate<String> transformableMethodPredicate;

	/**
	 * Constructor.
	 * 
	 * @param api                           the ASM API version
	 * @param transformableClassPredicate   predicate to determine if a class shall
	 *                                      be transformed or excluded
	 * @param aTransformableMethodPredicate predicate to determine if method shall
	 *                                      be transformed or excluded
	 */
	public AbstractMethodInClassTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> aTransformableMethodPredicate) {
		super(api, transformableClassPredicate);
		transformableMethodPredicate = aTransformableMethodPredicate;
	}

	/**
	 * Constructor with custom {@link RollbackTransformer}
	 * 
	 * @param api                           the ASM API version
	 * @param transformableClassPredicate   predicate to determine if a class shall
	 *                                      be transformed or excluded
	 * @param aTransformableMethodPredicate predicate to determine if method shall
	 *                                      be transformed or excluded
	 * @param rollbackTransformer           a custom {@link RollbackTransformer}
	 */
	public AbstractMethodInClassTransformer(final int api, final Predicate<String> transformableClassPredicate,
			final Predicate<String> aTransformableMethodPredicate, final RollbackTransformer rollbackTransformer) {
		super(api, transformableClassPredicate, rollbackTransformer);
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
