package io.github.super7ramp.faulty.agent.transformers.visitors;

import org.objectweb.asm.ClassVisitor;

/**
 * Dummy visitor.
 */
public final class DummyVisitor extends ClassVisitor {

	/**
	 * Constructor.
	 *
	 * @param api          the ASM API version
	 * @param classVisitor the visitor to delegate visit calls to
	 */
	public DummyVisitor(final int api, final ClassVisitor classVisitor) {
		super(api, classVisitor);
	}
}