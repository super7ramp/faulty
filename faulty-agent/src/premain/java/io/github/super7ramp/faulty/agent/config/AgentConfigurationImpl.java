package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link AgentConfiguration}.
 */
final class AgentConfigurationImpl implements AgentConfiguration {

	/** The transformable classes. */
	private final Collection<String> classesToPreTransform;

	/** Static bugs. */
	private final Collection<StaticBug> staticBugs;

	/** The ASM API version. */
	private final int api;

	/**
	 * Constructor.
	 * 
	 * @param someClassesToPreTransform the transformable classes
	 * @param someStaticBugs            the static bugs to apply at agent startup
	 * @param anApi                     the ASM API version
	 */
	AgentConfigurationImpl(final Collection<String> someClassesToPreTransform,
			final Collection<StaticBug> someStaticBugs, final int anApi) {
		classesToPreTransform = someClassesToPreTransform;
		staticBugs = someStaticBugs;
		api = anApi;
	}

	@Override
	public final int apiVersion() {
		return api;
	}

	@Override
	public final Collection<String> classesToPreTransform() {
		return Collections.unmodifiableCollection(classesToPreTransform);
	}

	@Override
	public final Collection<StaticBug> staticBugs() {
		return Collections.unmodifiableCollection(staticBugs);
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Agent conf: API = ").append(api);
		if (!classesToPreTransform.isEmpty()) {
			sb.append(", classes to pre-transform= ").append(classesToPreTransform);
		}
		if (!staticBugs.isEmpty()) {
			sb.append(", static bugs= ").append(staticBugs);
		}
		return sb.toString();
	}

}
