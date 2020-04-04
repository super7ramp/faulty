package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.transformers.RevertableClassFileTransformer;
import io.github.super7ramp.faulty.agent.transformers.Transformers;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.api.InfiniteLoopParameters;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RevertableBug;
import io.github.super7ramp.faulty.api.RuntimeExceptionParameters;

/**
 * Implementation of {@link FaultyServices}.
 */
// TODO create processors, too much code in this service class
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
	public final RevertableBug injectInfiniteLoop(final InfiniteLoopParameters parameters)
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
		return injectBug(parameters.className(), transformer);
	}

	@Override
	public final RevertableBug injectRuntimeException(final RuntimeExceptionParameters parameters)
			throws AgentNotLaunchedException, InjectionFailureException {
		LOGGER.info("Injecting runtime exception in " + parameters.className());
		final RevertableClassFileTransformer transformer = Transformers
				.runtimeExceptionTransformer(parameters.className()::equals, parameters.methodName()::equals);
		return injectBug(parameters.className(), transformer);
	}

	/**
	 * Inject bug an return the {@link RevertableBug} as result.
	 * 
	 * @param className   the name of the class to transform
	 * @param transformer the transformation to apply
	 * @return the injected bug
	 * @throws AgentNotLaunchedException if agent is not launched
	 * @throws InjectionFailureException if injection failed for the given class
	 */
	private RevertableBug injectBug(final String className, final RevertableClassFileTransformer transformer)
			throws AgentNotLaunchedException, InjectionFailureException {
		inject(className, transformer);
		return () -> inject(className, transformer.reverter());
	}

	/**
	 * The common boiler plate to inject.
	 * 
	 * @param className   the name of the class to transform
	 * @param transformer the transformation to apply
	 * @throws AgentNotLaunchedException if agent is not launched
	 * @throws InjectionFailureException if injection failed for the given class
	 */
	private void inject(final String className, final ClassFileTransformer transformer)
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
