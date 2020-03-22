package io.github.super7ramp.faulty.agent.transformers.visitors;

import org.objectweb.asm.MethodVisitor;

/**
 * Meh.
 */
public interface MethodVisitorFactory {

	MethodVisitor create(final MethodVisitor delegate);

}
