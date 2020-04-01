package io.github.super7ramp.faulty.agent.transformers;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

/**
 * A {@link ClassFileTransformer} that applies an original buffer, typically to
 * rollback a previous transformation.
 */
class RollbackTransformer implements ClassFileTransformer {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(RollbackTransformer.class.getName());

	/** The original class file buffer. */
	private byte[] originalClassFileBuffer;

	/**
	 * Constructor.
	 */
	RollbackTransformer() {
		originalClassFileBuffer = null;
	}

	@Override
	public final byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {

		if (originalClassFileBuffer != null) {
			LOGGER.info("Restoring original code for " + className);
			preTransform();
		}

		/*
		 * It's fine if buffer is null, it means that no transformation will be applied
		 * an that's what we want.
		 */
		return originalClassFileBuffer;
	}

	/**
	 * Set the {@link #originalClassFileBuffer} to apply upon transformation.
	 * 
	 * @param buffer the class file buffer to set
	 */
	final void setOriginalClassFileBuffer(final byte[] buffer) {
		originalClassFileBuffer = buffer;
	}

	/**
	 * Any action to be performed before actual transformation.
	 * <p>
	 * It can be - for example - an action to free active stack frames, to
	 * immediately restore normal behaviour (useful when working with infinite
	 * loops).
	 */
	protected void preTransform() {
		// Do nothing by default.
	}

}
