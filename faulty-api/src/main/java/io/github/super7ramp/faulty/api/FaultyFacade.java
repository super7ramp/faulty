package io.github.super7ramp.faulty.api;

/**
 * Access to agent services.
 */
public final class FaultyFacade {

	/**
	 * @return instance of {@link FaultyServices}
	 * @throws AgentNotFoundException if faulty agent is not found in classpath
	 */
	public static final FaultyServices getServices() throws AgentNotFoundException {
		try {
			final Class<?> c = Class.forName("io.github.super7ramp.faulty.agent.FaultyServicesImpl");
			return (FaultyServices) c.getConstructor().newInstance();
		} catch (final ReflectiveOperationException e) {
			throw new AgentNotFoundException(e);
		}
	}
}
