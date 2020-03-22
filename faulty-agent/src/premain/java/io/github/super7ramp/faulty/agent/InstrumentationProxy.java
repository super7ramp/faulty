package io.github.super7ramp.faulty.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashSet;
import java.util.Set;

import io.github.super7ramp.faulty.agent.config.AgentConfiguration;
import io.github.super7ramp.faulty.api.AgentNotLaunchedException;
import io.github.super7ramp.faulty.api.InjectionFailureException;

/**
 * Access to the {@link Instrumentation}.
 */
final class InstrumentationProxy {

	/** The added transformers. */
	private final Set<ClassFileTransformer> transformers;

	/** The instrumentation access. */
	private Instrumentation instrumentation;

	/** Agent configuration. */
	private AgentConfiguration conf;

	/**
	 * Instance holder (singleton).
	 */
	private static class InstanceHolder {
		static final InstrumentationProxy INSTANCE = new InstrumentationProxy();
	}

	/**
	 * Private constructor.
	 */
	private InstrumentationProxy() {
		transformers = new HashSet<>();
	}

	/**
	 * @return the unique instance of this access
	 */
	static InstrumentationProxy getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Inject the {@link Instrumentation} instance inside this holder.
	 *
	 * @param anInstrumentation the instance to inject
	 */
	void setInstrumentation(final Instrumentation anInstrumentation, final AgentConfiguration config) {
		if (instrumentation != null) {
			throw new IllegalStateException("Instrumentation already set");
		}
		instrumentation = anInstrumentation;
		conf = config;
	}

	void addTransformer(final ClassFileTransformer transformer) throws AgentNotLaunchedException {
		checkAgentLaunched();
		instrumentation.addTransformer(transformer, true);
		transformers.add(transformer);
	}

	void removeTransformer(final ClassFileTransformer transformer) throws AgentNotLaunchedException {
		checkAgentLaunched();
		instrumentation.removeTransformer(transformer);
		transformers.remove(transformer);
	}

	void removeAllTransformer() throws AgentNotLaunchedException {
		checkAgentLaunched();
		transformers.forEach(instrumentation::removeTransformer);
		transformers.clear();
	}

	void retransform(final Class<?>[] classes) throws AgentNotLaunchedException, InjectionFailureException {
		checkAgentLaunched();
		checkClassesAreRetransformable(classes);
		try {
			instrumentation.retransformClasses(classes);
		} catch (final UnmodifiableClassException e) {
			throw new InjectionFailureException(e);
		}
	}

	/**
	 * Check classes are retransformable.
	 * 
	 * @param classes class to retransform
	 * @throws InjectionFailureException if check fails
	 */
	private void checkClassesAreRetransformable(final Class<?>[] classes) throws InjectionFailureException {
		if (!instrumentation.isRetransformClassesSupported()) {
			throw new InjectionFailureException("Re-transformation not supported.");
		}
		for (final Class<?> clazz : classes) {
			final String className = clazz.getName();
			if (conf.transformableClassPrefix().stream().filter(className::startsWith).findAny().isEmpty()) {
				throw new InjectionFailureException(
						"Class " + className + " not configured as transformable. Check your agent configuration.");
			}
		}
	}

	/**
	 * Check that the agent is launched, i.e. instrumentation is not
	 * <code>null</code>.
	 * 
	 * @throws AgentNotLaunchedException if check fails
	 */
	private void checkAgentLaunched() throws AgentNotLaunchedException {
		if (instrumentation == null) {
			throw new AgentNotLaunchedException(null);
		}
	}
}
