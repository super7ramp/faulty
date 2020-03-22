package io.github.super7ramp.faulty.api;

/**
 * Access to agent services.
 */
public final class FaultyFacade {

	/**
	 * @return instance of {@link FaultyServices}
	 */
	public static final FaultyServices getServices() throws AgentNotFoundException {
		try {
			final Class<?> c = Class.forName("io.github.super7ramp.faulty.agent.FaultyServicesImpl");
			return (FaultyServices) c.getConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new AgentNotFoundException(e);
		}
	}
}
