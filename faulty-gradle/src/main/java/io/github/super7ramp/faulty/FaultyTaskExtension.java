package io.github.super7ramp.faulty;

import java.util.Collection;
import java.util.HashSet;

/**
 * Task extension to use faulty.
 * <p>
 * Fields are injected and need to be public.
 */
public class FaultyTaskExtension {

	/** The classes that should be pre-transformed. */
	public Collection<String> preTransform;

	/** Whether faulty should be enabled, default is false. */
	public boolean enabled;

	/**
	 * Constructor.
	 */
	public FaultyTaskExtension() {
		preTransform = new HashSet<>();
	}

	Iterable<String> preTransform() {
		return preTransform;
	}

	boolean isEnabled() {
		return enabled;
	}

	boolean hasArguments() {
		return !preTransform.isEmpty();
	}

}
