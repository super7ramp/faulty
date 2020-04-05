package io.github.super7ramp.faulty.agent.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;

import io.github.super7ramp.faulty.agent.config.StaticBug.Kind;

/**
 * Arguments:
 * <ul>
 * <li><em><tt>include=com.example.package.ClassNameA</tt></em>: Indicate that
 * <tt>ClassNameA</tt> is can be dynamically transformable. Repeatable. Can also
 * include package name.
 * <li><em><tt>api=6</tt></em>: Optional, indicate that API v6 of ASM should be
 * used. Default is 7.
 * </ul>
 * Arguments are separated by ','.
 */
public final class ArgumentParser {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(ArgumentParser.class.getName());

	/** The arguments to parse. */
	private final String args;

	/**
	 * Constructor.
	 * 
	 * @param arguments arguments
	 */
	public ArgumentParser(final String arguments) {
		args = arguments == null ? "" : arguments;
	}

	/**
	 * @return the agent configuration deduced from parsed arguments
	 */
	// don't judge me
	public final AgentConfiguration parse() {
		final Collection<String> transformableClassPrefixes = new HashSet<>();
		final Collection<StaticBug> staticBugs = new HashSet<>();
		int api = 7;
		for (final String splittedArguments : args.split(",")) {
			final String[] keyValue = splittedArguments.split("=");
			if (keyValue.length == 2) {
				final String key = keyValue[0];
				final String value = keyValue[1];
				switch (key) {
				case "preTransform":
					transformableClassPrefixes.add(value);
					break;
				case "api":
					api = Integer.parseInt(value);
					break;
				case "infiniteLoop":
					parseBug(value, Kind.INFINITE_LOOP).ifPresent(staticBugs::add);
					break;
				case "infiniteInterruptibleLoop":
					parseBug(value, Kind.INFINITE_INTERRUPTIBLE_LOOP).ifPresent(staticBugs::add);
					break;
				case "runtimeException":
					parseBug(value, Kind.RUNTIME_EXCEPTION).ifPresent(staticBugs::add);
					break;
				default:
					LOGGER.warning("Unknown key: " + key);
				}
			}
		}
		return new AgentConfigurationImpl(transformableClassPrefixes, staticBugs, api);
	}

	private static Optional<StaticBug> parseBug(final String location, final Kind kind) {
		final String[] splittedLocation = location.split("\\.");
		if (splittedLocation.length < 2) {
			return Optional.empty();
		}
		final int methodNameIndex = splittedLocation.length - 1;
		final String methodName = splittedLocation[methodNameIndex];
		final String className = String.join(".",
				Arrays.copyOfRange(splittedLocation, 0, methodNameIndex /* exclusive. */));
		final StaticBug bug = new StaticBugImpl(className, methodName, kind);
		return Optional.of(bug);
	}

}
