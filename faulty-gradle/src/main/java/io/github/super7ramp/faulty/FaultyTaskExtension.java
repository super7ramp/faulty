package io.github.super7ramp.faulty;

import java.util.Collection;
import java.util.HashSet;

import org.gradle.api.tasks.Input;

/**
 * Task extension to use faulty.
 */
public class FaultyTaskExtension {

	/** The classes that should be pre-transformed. */
	private final Collection<String> preTransform;

	/** Whether faulty should be enabled, default is false. */
	private boolean enabled;

	/**
	 * Constructor.
	 * 
	 * @param agentConfiguration the agent configuration
	 */
	public FaultyTaskExtension() {
		preTransform = new HashSet<>();
	}

	@Input
	public Iterable<String> preTransform() {
		return preTransform;
	}

	@Input
	public boolean isEnabled() {
		return enabled;
	}

	boolean hasArguments() {
		return !preTransform.isEmpty();
	}

}
