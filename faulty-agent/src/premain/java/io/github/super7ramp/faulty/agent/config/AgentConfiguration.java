package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;

/**
 * Agent configuration.
 */
public interface AgentConfiguration {

	/**
	 * @return the class names to be pre-transformed
	 */
	Collection<String> classesToPreTransform();

	/**
	 * @return the bugs to apply at agent start
	 */
	Collection<StaticBug> staticBugs();

	/**
	 * @return the ASM API version to use
	 */
	int apiVersion();

}
