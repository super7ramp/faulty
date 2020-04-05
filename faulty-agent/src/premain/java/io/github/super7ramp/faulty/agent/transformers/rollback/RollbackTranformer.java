package io.github.super7ramp.faulty.agent.transformers.rollback;

import java.lang.instrument.ClassFileTransformer;

/**
 * 
 */
public interface RollbackTranformer extends ClassFileTransformer {

	/**
	 * Set the {@link #originalClassFileBuffer} to apply upon transformation.
	 *
	 * @param className class name
	 * @param buffer    the class file buffer to set
	 */
	void setOriginalClassFileBuffer(final String className, final byte[] buffer);

}
