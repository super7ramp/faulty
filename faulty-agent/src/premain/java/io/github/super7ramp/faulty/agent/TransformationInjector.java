package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import io.github.super7ramp.faulty.agent.transformers.RevertableClassFileTransformer;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.InjectionFailureException;
import io.github.super7ramp.faulty.api.RevertableTransformation;

/**
 * The common boilerplate to retransform classes.
 */
final class TransformationInjector {

	/** Access to instrumentation. */
	private final InstrumentationProxy instrumentationProxy;

	/**
	 * Constructor.
	 * 
	 * @param anInstrumentationProxy access to Instrumentation
	 */
	TransformationInjector(final InstrumentationProxy anInstrumentationProxy) {
		instrumentationProxy = anInstrumentationProxy;
	}

	/**
	 * Inject bug an return the {@link RevertableTransformation} as result.
	 * 
	 * @param className   the name of the class to transform
	 * @param transformer the transformation to apply
	 * @return the injected bug
	 * @throws AgentNotLaunchedException if agent is not launched
	 * @throws InjectionFailureException if injection failed for the given class
	 */
	RevertableTransformation injectTransformation(final String className,
			final RevertableClassFileTransformer transformer)
			throws AgentNotLaunchedException, InjectionFailureException {
		final Iterable<String> classNames = Arrays.asList(className);
		inject(classNames, transformer);
		return () -> inject(classNames, transformer.reverter());
	}

	/**
	 * Inject a transformation and return the {@link RevertableTransformation} as
	 * result.
	 * 
	 * @param classNames  the names of the class to transform
	 * @param transformer the transformation to apply
	 * @return the injected bug
	 * @throws AgentNotLaunchedException if agent is not launched
	 * @throws InjectionFailureException if injection failed for the given class
	 */
	RevertableTransformation injectTransformation(final Iterable<String> classNames,
			final RevertableClassFileTransformer transformer)
			throws AgentNotLaunchedException, InjectionFailureException {
		inject(classNames, transformer);
		return () -> inject(classNames, transformer.reverter());
	}

	/**
	 * The common boiler plate to inject.
	 * 
	 * @param classNames  the names of the class to transform
	 * @param transformer the transformation to apply
	 * @throws AgentNotLaunchedException if agent is not launched
	 * @throws InjectionFailureException if injection failed for the given class
	 */
	private void inject(final Iterable<String> classNames, final ClassFileTransformer transformer)
			throws AgentNotLaunchedException, InjectionFailureException {

		final Collection<Class<?>> classes = new ArrayList<>();
		for (final String prefix : classNames) {
			try {
				classes.add(Class.forName(prefix));
			} catch (final ClassNotFoundException e) {
				throw new InjectionFailureException(e);
			}
		}

		instrumentationProxy.addTransformer(transformer);
		try {
			instrumentationProxy.retransform(classes.toArray(new Class<?>[0]));
		} finally {
			instrumentationProxy.removeTransformer(transformer);
		}
	}
}
