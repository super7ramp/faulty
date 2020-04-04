package io.github.super7ramp.faulty.agent.transformers.visitors;

import org.objectweb.asm.MethodVisitor;

/**
 * Meh.
 */
public interface MethodVisitorFactory {

	/**
	 * Create a {@link MethodVisitor} using given visitor as delegate.
	 * 
	 * @param delegate          delegate method visitor
	 * @param visitedMethodName the name of the visited method
	 * @return the desired method visitor
	 */
	MethodVisitor create(final MethodVisitor delegate, final String visitedMethodName);

}
