package io.github.super7ramp.faulty.spi;

import io.github.super7ramp.faulty.api.FaultyServices;

/**
 * Faulty services provider.
 */
public interface FaultyServicesProvider {

	/**
	 * @return instance of {@link FaultyServices}
	 */
	FaultyServices getServices();

}
