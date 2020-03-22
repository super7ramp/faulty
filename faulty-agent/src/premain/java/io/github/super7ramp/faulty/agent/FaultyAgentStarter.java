package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.config.AgentConfiguration;
import io.github.super7ramp.faulty.agent.config.ArgumentParser;
import io.github.super7ramp.faulty.agent.transformers.Transformers;

/**
 * Faulty agent starter.
 */
public final class FaultyAgentStarter {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(FaultyAgentStarter.class.getName());

	/** Instrumentation utilities. */
	private final Instrumentation instrumentation;

	/**
	 * Constructor.
	 * 
	 * @param anInstrumentation the instrumentation utilities
	 */
	public FaultyAgentStarter(final Instrumentation anInstrumentation) {
		instrumentation = anInstrumentation;
	}

	/**
	 * Start agent.
	 * 
	 * @param arguments arguments passed in command line
	 */
	public void start(final String arguments) {
		LOGGER.info("Agent starting...");

		/*
		 * 1. Read conf specified in arguments.
		 */
		final AgentConfiguration conf = new ArgumentParser(arguments).parse();
		LOGGER.info(conf.toString());

		/*
		 * 2. Apply pre-transformation with NoOpTransformer to the classes susceptible
		 * to be transformed later.
		 */
		preTransform(conf.transformableClassPrefix());

		/*
		 * 3. Apply potential bugs specified statically in arguments.
		 */
		// TODO

		/*
		 * 4. Store info inside InstrumentationProxy so they can be dynamically used
		 */
		InstrumentationProxy.getInstance().setInstrumentation(instrumentation, conf);
		LOGGER.info("Agent started.");
	}

	/**
	 * TODO check if it's really necessary and if yes, finish implementation.
	 * 
	 * @param transformableClassPrefix
	 */
	private void preTransform(final Collection<String> transformableClassPrefix) {
		final Predicate<String> transformableClassPredicate = (className) -> transformableClassPrefix.stream()
				.filter(className::startsWith).findAny().isPresent();
		final ClassFileTransformer transformer = Transformers.dummyTransformer(transformableClassPredicate);

		/*
		 * Build class list to transform.
		 */
		final Collection<Class<?>> classes = new ArrayList<>();
		for (final String prefix : transformableClassPrefix) {
			final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			final Package definedPackage = classLoader.getDefinedPackage(prefix);
			if (definedPackage != null) {
				// Transform all classes for this package
				// TODO not implemented yet
				throw new UnsupportedOperationException(definedPackage.toString());
			} else {
				// Not a package. Check if it's a class.
				try {
					classes.add(Class.forName(prefix));
				} catch (final ClassNotFoundException e) {
					LOGGER.warning(prefix + " is not a known class, ignoring");
				}
			}
		}

		if (!classes.isEmpty()) {
			/*
			 * Force transformation.
			 */
			instrumentation.addTransformer(transformer, true);
			try {
				LOGGER.info("Pre-transforming " + classes);
				instrumentation.retransformClasses(classes.toArray(new Class<?>[0]));
			} catch (final UnmodifiableClassException e) {
				throw new IllegalStateException(e);
			} finally {
				instrumentation.removeTransformer(transformer);
			}
		}

	}
}
