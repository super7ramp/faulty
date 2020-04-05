package io.github.super7ramp.faulty.agent.transformers.rollback;

/**
 * Rollback transformer factory.
 */
public final class RollbackTransformers {

	/**
	 * Constructor.
	 */
	private RollbackTransformers() {
		// Nothing to do.
	}

	/**
	 * Creates a default rollback transformer, suitable for classic transformation
	 * rollback.
	 *
	 * @return a default rollback transformer
	 */
	public static final RollbackTranformer defaultRollbackTransformer() {
		return new DefaultRollbackTransformer();
	}

	/**
	 * Creates a rollback transformer suitable for infinite loop transformation
	 * rollback: It breaks the injected infinite loop in order to free the active
	 * stack frames using old bytecode.
	 *
	 * @return a transformer suitable to rollback an infinite loop transformation
	 */
	public static final ListeningRollbackTransformer infiniteLoopRollbackTransformer() {
		return new InfiniteLoopRollbackTransformer();
	}

}
