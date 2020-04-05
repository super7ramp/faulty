package io.github.super7ramp.faulty.agent;

import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.transformers.RevertableClassFileTransformer;
import io.github.super7ramp.faulty.agent.transformers.Transformers;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InfiniteLoopParameters;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RevertableTransformation;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * Implementation of {@link FaultyServices}.
 */
public final class FaultyServicesImpl implements FaultyServices {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(FaultyServicesImpl.class.getName());

	/** The actual processor. */
	private final TransformationInjector bugInjector;

	/**
	 * Constructor.
	 */
	public FaultyServicesImpl() {
		bugInjector = new TransformationInjector(InstrumentationProxy.getInstance());
	}

	@Override
	public final RevertableTransformation injectInfiniteLoop(final InfiniteLoopParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException {
		LOGGER.info("Injecting infinite loop in " + parameters.className());
		final RevertableClassFileTransformer transformer;
		if (parameters.interruptible()) {
			transformer = Transformers.interruptibleInfiniteLoopTransformer(parameters.className()::equals,
					parameters.methodName()::equals);
		} else {
			transformer = Transformers.infiniteLoopTransformer(parameters.className()::equals,
					parameters.methodName()::equals);
		}
		return bugInjector.injectTransformation(parameters.className(), transformer);
	}

	@Override
	public final RevertableTransformation injectRuntimeException(final RuntimeExceptionParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException {
		LOGGER.info("Injecting runtime exception in " + parameters.className());
		final RevertableClassFileTransformer transformer = Transformers
				.runtimeExceptionTransformer(parameters.className()::equals, parameters.methodName()::equals);
		return bugInjector.injectTransformation(parameters.className(), transformer);
	}

}
