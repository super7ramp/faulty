package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;

public interface AgentConfiguration {

	/**
	 * @return the class names to be pre-transformed
	 */
	Collection<String> classesToPreTransform();

//	/**
//	 * TODO return the bug to apply at agent start (i.e. in premain)
//	 */
	// BugTasks staticBugs();

	/**
	 * @return the ASM API version to use
	 */
	int apiVersion();

}
