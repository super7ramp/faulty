package io.github.super7ramp.faulty.api;

/**
 * Access to agent services.
 */
public final class FaultyFacade {

	/**
	 * Name of the actual implementation of {@link FaultyServices}, i.e. provided by
	 * the java agent.
	 */
	private static final String SERVICE_IMPLEMENTATION_CLASS = "io.github.super7ramp.faulty.agent.FaultyServicesImpl";

	/**
	 * Private constructor.
	 */
	private FaultyFacade() {
		// empty.
	}

	/**
	 * @return instance of {@link FaultyServices}
	 * @throws AgentNotFoundException if faulty agent is not found in classpath
	 */
	public static final FaultyServices getServices() throws AgentNotFoundException {
		try {
			final Class<?> c = Class.forName(SERVICE_IMPLEMENTATION_CLASS);
			return (FaultyServices) c.getConstructor().newInstance();
		} catch (final ReflectiveOperationException e) {
			throw new AgentNotFoundException(e);
		}
	}
}
