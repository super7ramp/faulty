package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Collection;
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
		preTransform(conf.classesToPreTransform());

		/*
		 * 3. Apply potential bugs specified statically in arguments.
		 */
		// TODO

		/*
		 * 4. Store info inside InstrumentationProxy so they can be dynamically used
		 */
		InstrumentationProxy.getInstance().attach(instrumentation);
		LOGGER.info("Agent started.");
	}

	/**
	 * Pre-transform class, i.e. just apply the default ASM class visitor.
	 *
	 * @param classesToPreTransform the classes to pre-transform
	 */
	private void preTransform(final Collection<String> classesToPreTransform) {
		/*
		 * Build class list to transform.
		 */
		final Collection<Class<?>> classes = new ArrayList<>();
		for (final String prefix : classesToPreTransform) {
			try {
				classes.add(Class.forName(prefix));
			} catch (final ClassNotFoundException e) {
				LOGGER.warning(prefix + " is not a known class, it will no be pre-transformed");
			}
		}

		if (!classes.isEmpty()) {
			final ClassFileTransformer transformer = Transformers.dummyTransformer(classesToPreTransform::contains);
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
