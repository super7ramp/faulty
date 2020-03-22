package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;

public interface AgentConfiguration {

	/**
	 * @return the class package names susceptible to be transformed
	 */
	Collection<String> transformableClassPrefix();

	/**
	 * @return the API version to use
	 */
	int apiVersion();

}
