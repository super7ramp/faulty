package io.github.super7ramp.faulty.agent.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

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
	public final AgentConfiguration parse() {
		final Collection<String> transformableClassPrefixes = new HashSet<>();
		int api = 7;
		for (final String splittedArguments : args.split(",")) {
			final String[] keyValue = splittedArguments.split("=");
			if (keyValue.length == 2) {
				final String key = keyValue[0];
				final String value = keyValue[1];
				switch (key) {
				case "include":
					transformableClassPrefixes.add(value);
					break;
				case "api":
					api = Integer.parseInt(value);
					break;
				default:
					LOGGER.warning("Unknown key: " + key);
				}
			}
		}
		return new AgentConfigurationImpl(transformableClassPrefixes, api);
	}

}
