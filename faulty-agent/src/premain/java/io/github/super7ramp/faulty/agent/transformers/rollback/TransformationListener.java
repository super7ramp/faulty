package io.github.super7ramp.faulty.agent.transformers.rollback;

/**
 * A transformation listener.
 */
public interface TransformationListener {

	/**
	 * Notify the transformation occurred.
	 * 
	 * @param className  the transformed class name
	 * @param methodName the transformed method name
	 */
	void notifyTransformation(final String className, final String methodName);

}
