package io.github.super7ramp.faulty.agent.transformers.rollback;

import java.lang.instrument.ClassFileTransformer;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A {@link ClassFileTransformer} that applies an original buffer, typically to
 * rollback a previous transformation.
 * <p>
 * Can be specialized by implementing a custom {@link #preTransform(String)}
 * method.
 */
class DefaultRollbackTransformer implements RollbackTranformer {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultRollbackTransformer.class.getName());

	/** The original class file buffers. */
	private final Map<String, ByteBuffer> originalClassFileBuffers;

	/**
	 * Constructor.
	 */
	DefaultRollbackTransformer() {
		originalClassFileBuffers = new HashMap<>();
	}

	@Override
	public final byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {

		final byte[] rollbackedBuffer;
		final ByteBuffer originalClassFileBuffer = originalClassFileBuffers.remove(className);
		if (originalClassFileBuffer != null) {
			LOGGER.info("Restoring original code for " + className);
			preTransform(className);
			rollbackedBuffer = originalClassFileBuffer.array();
		} else {
			/*
			 * It's fine if buffer is null, it means that no transformation will be applied
			 * an that's what we want.
			 */
			rollbackedBuffer = null;
		}

		return rollbackedBuffer;
	}

	/**
	 * Set the {@link #originalClassFileBuffer} to apply upon transformation.
	 *
	 * @param className class name
	 * @param buffer    the class file buffer to set
	 */
	@Override
	public final void setOriginalClassFileBuffer(final String className, final byte[] buffer) {
		if (originalClassFileBuffers.containsKey(className)) {
			throw new IllegalStateException("Rollback transformer must be linked to exactly one original transformer"
					+ " (i.e. possibly several classes transformed but only one transformation per class)");
		}
		originalClassFileBuffers.put(className, ByteBuffer.wrap(buffer));
	}

	/**
	 * Any action to be performed before actual transformation.
	 * <p>
	 * Typically this is an action to free or drop active stack frames, i.e. make
	 * sure that no execution with old code will continue (useful when working with
	 * infinite loops).
	 *
	 * @param className the name of the class being rollbacked
	 */
	protected void preTransform(final String className) {
		// Do nothing by default.
	}

}
