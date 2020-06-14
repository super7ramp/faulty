package io.github.super7ramp.faulty.api;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

import io.github.super7ramp.faulty.spi.FaultyServicesProvider;

/**
 * Access to agent services.
 */
public final class Faulty {

	/** Service implementation loader. */
	private static final ServiceLoader<FaultyServicesProvider> LOADER = ServiceLoader
			.load(FaultyServicesProvider.class);

	/**
	 * Private constructor.
	 */
	private Faulty() {
		// empty.
	}

	/**
	 * Get access to agent services.
	 * 
	 * @return instance of {@link FaultyServices}
	 * @throws AgentNotFoundException if no {@link FaultyServices} implementation
	 *                                found
	 */
	public static final FaultyServices getServices() throws AgentNotFoundException {
		return findFirst().map(FaultyServicesProvider::getServices).orElseThrow(AgentNotFoundException::new);
	}

	/**
	 * Find first implementation of {@link FaultyServicesProvider}.
	 * 
	 * @return The first service provider or empty {@code Optional} if no service
	 *         providers are located
	 */
	private static Optional<FaultyServicesProvider> findFirst() {
		final Iterator<FaultyServicesProvider> serviceProviders = LOADER.iterator();
		if (serviceProviders.hasNext()) {
			return Optional.of(serviceProviders.next());
		} else {
			return Optional.empty();
		}
	}
}
