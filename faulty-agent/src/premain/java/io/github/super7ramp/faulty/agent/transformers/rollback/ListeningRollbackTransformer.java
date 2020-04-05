package io.github.super7ramp.faulty.agent.transformers.rollback;

/**
 * A {@link RollbackTranformer} which can perform some action when the original
 * transformation occurs because it extends {@link TransformationListener}.
 */
public interface ListeningRollbackTransformer extends RollbackTranformer, TransformationListener {
	// Marker interface
}
