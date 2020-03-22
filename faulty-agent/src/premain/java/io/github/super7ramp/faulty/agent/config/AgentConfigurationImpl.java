package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link AgentConfiguration}.
 */
final class AgentConfigurationImpl implements AgentConfiguration {

	/** The transformable classes. */
	private final Collection<String> transformableClassPrefixes;

	/** The ASM API version. */
	private final int api;

	/**
	 * Constructor.
	 * 
	 * @param someTransformableClassPrefixes the transformable classes
	 * @param anApi                          the ASM API version
	 */
	AgentConfigurationImpl(final Collection<String> someTransformableClassPrefixes, final int anApi) {
		transformableClassPrefixes = someTransformableClassPrefixes;
		api = anApi;
	}

	@Override
	public final Collection<String> transformableClassPrefix() {
		return Collections.unmodifiableCollection(transformableClassPrefixes);
	}

	@Override
	public final int apiVersion() {
		return api;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Agent conf: API = ").append(api);
		sb.append(", include = ").append(transformableClassPrefixes);
		return sb.toString();
	}

}
