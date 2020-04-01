package io.github.super7ramp.faulty.agent.transformers;

import java.lang.instrument.ClassFileTransformer;

/**
 * A {@link ClassFileTransformer} which provides its opposite sibling, i.e.
 * another transformer able to rollback the first transformation.
 */
public interface RevertableClassFileTransformer extends ClassFileTransformer {

	/**
	 * The {@link ClassFileTransformer} that will revert this transformer.
	 * <p>
	 * Applying the transformation with the reverter whereas the main transformer
	 * has not been applied will do nothing.
	 * 
	 * @return the {@link ClassFileTransformer} that will revert this transformer
	 */
	ClassFileTransformer reverter();

}
