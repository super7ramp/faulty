package io.github.super7ramp.faulty.agent.transformers;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * Base class for {@link ClassFileTransformer} implementations.
 */
abstract class AbstractTransformer implements ClassFileTransformer {

	/** No flag. */
	private static final int NO_FLAG = 0;

	/** Logger. **/
	private static final Logger LOGGER = Logger.getLogger(AbstractTransformer.class.getName());

	/** The ASM API version to use. */
	private final int api;

	/** Filter on class name. */
	private final Predicate<String> transformableClass;

	/**
	 * Constructor.
	 *
	 * @param apiVersion                  the ASM API version to use
	 * @param transformableClassPredicate predicate to determine if a class shall be
	 *                                    transformed or excluded
	 */
	AbstractTransformer(final int apiVersion, final Predicate<String> transformableClassPredicate) {
		transformableClass = transformableClassPredicate;
		api = intToOpCode(apiVersion);
	}

	/**
	 * Return the right ASM API OpCode.
	 * 
	 * @param apiVersion the API version read from arguments
	 * @return the checked API version
	 */
	private static int intToOpCode(final int apiVersion) {
		switch (apiVersion) {
		case 4:
			return Opcodes.ASM4;
		case 5:
			return Opcodes.ASM5;
		case 6:
			return Opcodes.ASM6;
		default:
			return Opcodes.ASM7;
		}
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
			final PrintWriter pw = new PrintWriter(System.out);
			final ClassVisitor dumpVisitor = new TraceClassVisitor(writer, pw);
			final ClassVisitor visitor = classVisitor(api, dumpVisitor);
			reader.accept(visitor, NO_FLAG);
			return writer.toByteArray();
		} catch (final IOException e) {
			LOGGER.log(Level.WARNING,
					"Transformation failed for class " + className + " with transformer " + actualTransformerName, e);
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
