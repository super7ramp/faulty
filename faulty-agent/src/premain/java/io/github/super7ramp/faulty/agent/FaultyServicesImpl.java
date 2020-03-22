package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.transformers.Transformers;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InfiniteLoopParameters;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * Implementation of {@link FaultyServices}.
 */
public final class FaultyServicesImpl implements FaultyServices {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(FaultyServicesImpl.class.getName());

	/** Instrumentation access. */
	private final InstrumentationProxy instrumentationProxy;

	/**
	 * Constructor.
	 */
	public FaultyServicesImpl() {
		instrumentationProxy = InstrumentationProxy.getInstance();
	}

	@Override
	public final void injectInfiniteLoop(final InfiniteLoopParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException {
		LOGGER.info("Injecting infinite loop in " + parameters.className());
		final ClassFileTransformer transformer = Transformers.infiniteLoopTransformer(parameters.className()::equals,
				parameters.methodName()::equals);
		injectAndTransform(parameters.className(), transformer);
	}

	@Override
	public final void injectRuntimeException(final RuntimeExceptionParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException {
		LOGGER.info("Injecting runtime exception in " + parameters.className());
		final ClassFileTransformer transformer = Transformers
				.runtimeExceptionTransformer(parameters.className()::equals, parameters.methodName()::equals);
		injectAndTransform(parameters.className(), transformer);
	}

	/**
	 * The common boiler plate to inject.
	 * <p>
	 * TODO move out, this shall not be in service class
	 */
	private void injectAndTransform(final String className, final ClassFileTransformer transformer)
			throws AgentNotLaunchedException, InjectionFailureException {
		instrumentationProxy.addTransformer(transformer);
		try {
			final Class<?> clazz = Class.forName(className);
			final Class<?>[] classes = new Class<?>[] { clazz };
			instrumentationProxy.retransform(classes);
		} catch (final ClassNotFoundException e) {
			throw new InjectionFailureException(e);
		} finally {
			instrumentationProxy.removeTransformer(transformer);
		}
	}

}
