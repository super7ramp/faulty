package io.github.super7ramp.faulty.agent.transformers.visitors;

import java.util.function.Predicate;
import java.util.logging.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Apply a method visitor.
 */
public final class MethodInClassVisitor extends ClassVisitor {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(MethodInClassVisitor.class.getName());

	/** The predicate on method name. */
	private final Predicate<String> transformableMethod;

	/** Method visitor to create and apply if predicate is satisfied. */
	private final MethodVisitorFactory methodVisitorFactory;

	/**
	 * Constructor.
	 * 
	 * @param api                          the ASM API version
	 * @param classVisitor                 the visitor to delegate visit calls to
	 * @param transformableMethodPredicate the filter on method name
	 */
	public MethodInClassVisitor(final int api, final ClassVisitor classVisitor,
			final Predicate<String> transformableMethodPredicate, final MethodVisitorFactory aMethodVisitorFactory) {
		super(api, classVisitor);
		transformableMethod = transformableMethodPredicate;
		methodVisitorFactory = aMethodVisitorFactory;
	}

	@Override
	public final MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		final MethodVisitor delegateMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
		LOGGER.info("Visiting method " + name);
		if (transformableMethod.test(name)) {
			return methodVisitorFactory.create(delegateMethodVisitor);
		}
		return delegateMethodVisitor;
	}
}