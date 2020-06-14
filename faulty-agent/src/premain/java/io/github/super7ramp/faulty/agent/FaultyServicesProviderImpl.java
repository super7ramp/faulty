/**
 * 
 */
package io.github.super7ramp.faulty.agent;

import io.github.super7ramp.faulty.api.FaultyServices;
import io.github.super7ramp.faulty.spi.FaultyServicesProvider;

/**
 * {@link FaultyServicesProvider} implementation.
 */
public final class FaultyServicesProviderImpl implements FaultyServicesProvider {

	/**
	 * Constructor.
	 */
	public FaultyServicesProviderImpl() {
		// Nothing to do.
	}

	@Override
	public final FaultyServices getServices() {
		return new FaultyServicesImpl();
	}

}
