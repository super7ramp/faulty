package io.github.super7ramp.faulty.agent.transformers;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Base class for {@link ClassFileTransformer} implementations.
 */
abstract class AbstractTransformer implements ClassFileTransformer {

	/** No flag. */
	private static final int NO_FLAG = 0;

	/** Logger. **/
	private static final Logger LOGGER = Logger.getLogger(AbstractTransformer.class.getName());

	/** Filter on class name. */
	private final Predicate<String> transformableClass;

	/**
	 * Constructor.
	 *
	 * @param transformableClassPredicate predicate to determine if a class shall be
	 *                                    transformed or excluded
	 */
	public AbstractTransformer(final Predicate<String> transformableClassPredicate) {
		transformableClass = transformableClassPredicate;
	}

	@Override
	public final byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {

		if (transformableClass.negate().test(className.replace('/', '.'))) {
			return classfileBuffer;
		}

		final String actualTransformerName = this.getClass().getSimpleName();
		try {
			LOGGER.info("Transforming " + className + " with " + actualTransformerName);
			final ClassReader reader = new ClassReader(className);
			final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
			final ClassVisitor visitor = classVisitor(Opcodes.ASM7, writer);
			reader.accept(visitor, NO_FLAG);
			return writer.toByteArray();
		} catch (final IOException e) {
			LOGGER.log(Level.WARNING, "ouch", e);
			LOGGER.warning(
					"Transformation failed for class " + className + " with transformer " + actualTransformerName);
			return classfileBuffer;
		}
	}

	/**
	 * The method visitor the abstraction will call.
	 *
	 * @param api      the ASM API version
	 * @param delegate the visitor to delegate visit calls to (ultimately, the class
	 *                 writer)
	 * @return the class visitor
	 */
	protected abstract ClassVisitor classVisitor(final int api, final ClassVisitor delegate);

}
