package io.github.super7ramp.faulty.agent.transformers.rollback;

import java.lang.instrument.ClassFileTransformer;

/**
 * A {@link ClassFileTransformer} that applies an original buffer in order to
 * rollback a previous transformation.
 */
public interface RollbackTranformer extends ClassFileTransformer {

	/**
	 * Set the {@link #originalClassFileBuffer} to apply upon rollback.
	 *
	 * @param className class name
	 * @param buffer    the class file buffer to set
	 */
	void setOriginalClassFileBuffer(final String className, final byte[] buffer);

}
