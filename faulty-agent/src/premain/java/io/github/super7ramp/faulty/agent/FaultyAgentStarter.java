package io.github.super7ramp.faulty.agent;

import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.config.AgentConfiguration;
import io.github.super7ramp.faulty.agent.config.ArgumentParser;
import io.github.super7ramp.faulty.agent.config.StaticBug;
import io.github.super7ramp.faulty.agent.transformers.RevertableClassFileTransformer;
import io.github.super7ramp.faulty.agent.transformers.Transformers;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.InjectionFailureException;

/**
 * Faulty agent starter.
 */
final class FaultyAgentStarter {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(FaultyAgentStarter.class.getName());

	/** Instrumentation proxy. */
	private final TransformationInjector injector;

	/**
	 * Constructor.
	 * 
	 * @param instrumentation the instrumentation utilities
	 */
	FaultyAgentStarter(final Instrumentation instrumentation) {
		/*
		 * Attach actual Instrumentation to InstrumentationProxy so that it can be
		 * accessed later, from the rest of this class as well as from elsewhere.
		 */
		final InstrumentationProxy instrumentationProxy = InstrumentationProxy.getInstance();
		instrumentationProxy.attach(instrumentation);

		// The boilerplate code to transform classes
		injector = new TransformationInjector(instrumentationProxy);
	}

	/**
	 * Start agent.
	 * 
	 * @param arguments arguments passed in command line
	 */
	void start(final String arguments) {
		LOGGER.info("Agent starting...");

		/*
		 * 1. Read conf specified in arguments.
		 */
		final AgentConfiguration conf = new ArgumentParser(arguments).parse();
		LOGGER.info(conf.toString());

		/*
		 * 2. Apply pre-transformation to the classes susceptible to be transformed
		 * later.
		 */
		preTransform(conf.classesToPreTransform());

		/*
		 * 3. Apply potential bugs specified statically in arguments.
		 */
		injectStaticBugs(conf.staticBugs());

		LOGGER.info("Agent started.");
	}

	/**
	 * Inject bugs as defined in configuration.
	 * 
	 * @param staticBugs bugs as defined in configuration
	 */
	private void injectStaticBugs(final Iterable<StaticBug> staticBugs) {
		for (final StaticBug bug : staticBugs) {
			final RevertableClassFileTransformer transformer = bugToTransformer(bug);
			try {
				injector.injectTransformation(bug.className(), transformer);
			} catch (final InjectionFailureException e) {
				LOGGER.warning("A static bug injection failed: " + e.getMessage());
			} catch (final AgentNotLaunchedException e) {
				/*
				 * That should not happen since instrumentation proxy is properly initialized in
				 * constructor.
				 */
				throw new IllegalStateException(e);
			}
		}
	}

	/**
	 * Returns the appropriate {@link RevertableClassFileTransformer} from given
	 * {@link StaticBug}.
	 * 
	 * @param bug the bug
	 * @return the corresponding transformer
	 */
	private static RevertableClassFileTransformer bugToTransformer(final StaticBug bug) {
		final RevertableClassFileTransformer transformer;
		switch (bug.kind()) {
		case INFINITE_INTERRUPTIBLE_LOOP:
			transformer = Transformers.interruptibleInfiniteLoopTransformer(bug.className()::equals,
					bug.methodName()::equals);
			break;
		case INFINITE_LOOP:
			transformer = Transformers.infiniteLoopTransformer(bug.className()::equals, bug.methodName()::equals);
			break;
		case RUNTIME_EXCEPTION:
			transformer = Transformers.runtimeExceptionTransformer(bug.className()::equals, bug.methodName()::equals);
			break;
		default:
			throw new IllegalStateException("Unreachable code.");
		}
		return transformer;
	}

	/**
	 * Pre-transform class, i.e. just apply the default ASM class visitor.
	 *
	 * @param classesToPreTransform the classes to pre-transform
	 */
	private void preTransform(final Collection<String> classesToPreTransform) {
		if (!classesToPreTransform.isEmpty()) {
			final RevertableClassFileTransformer transformer = Transformers
					.dummyTransformer(classesToPreTransform::contains);
			try {
				injector.injectTransformation(classesToPreTransform, transformer);
			} catch (final InjectionFailureException e) {
				LOGGER.warning("Pre-transformation failed: " + e.getMessage());
			} catch (final AgentNotLaunchedException e) {
				/*
				 * That should not happen since instrumentation proxy is properly initialized in
				 * constructor.
				 */
				throw new IllegalStateException(e);
			}
		}
	}
}
