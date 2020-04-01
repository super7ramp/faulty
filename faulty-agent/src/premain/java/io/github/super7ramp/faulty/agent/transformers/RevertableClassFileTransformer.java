package io.github.super7ramp.faulty.agent.transformers;

import java.lang.instrument.ClassFileTransformer;

/**
 * A {@link ClassFileTransformer} which provides another
 * {@link ClassFileTransformer} as a way to revert its transformation.
 */
public interface RevertableClassFileTransformer extends ClassFileTransformer {

	/**
	 * The {@link ClassFileTransformer} that will revert this transformer. Note that
	 * trying to apply the "revert transformer" whereas the main transformer has not
	 * been applied will result into an {@link IllegalStateException}.
	 * 
	 * @return the {@link ClassFileTransformer} that will revert this transformer
	 */
	ClassFileTransformer reverter();

}
