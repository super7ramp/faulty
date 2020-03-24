package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link AgentConfiguration}.
 */
final class AgentConfigurationImpl implements AgentConfiguration {

	/** The transformable classes. */
	private final Collection<String> classesToPreTransform;

	/** The ASM API version. */
	private final int api;

	/**
	 * Constructor.
	 * 
	 * @param someClassesToPreTransform the transformable classes
	 * @param anApi                     the ASM API version
	 */
	AgentConfigurationImpl(final Collection<String> someClassesToPreTransform, final int anApi) {
		classesToPreTransform = someClassesToPreTransform;
		api = anApi;
	}

	@Override
	public final Collection<String> classesToPreTransform() {
		return Collections.unmodifiableCollection(classesToPreTransform);
	}

	@Override
	public final int apiVersion() {
		return api;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Agent conf: API = ").append(api);
		if (!classesToPreTransform.isEmpty()) {
			sb.append(", classes to pre-transform= ").append(classesToPreTransform);
		}
		return sb.toString();
	}

}
