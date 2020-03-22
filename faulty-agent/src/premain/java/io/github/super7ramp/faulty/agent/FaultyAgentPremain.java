/**
 * 
 */
package io.github.super7ramp.faulty.agent;

import java.lang.instrument.Instrumentation;

/**
 * This is the faulty agent entry point.
 * <p>
 * Only static access is supported, i.e. the agent must be launched with the
 * <code>-javaagent</code> command line parameter.
 */
public final class FaultyAgentPremain {

	/**
	 * Private constructor.
	 */
	private FaultyAgentPremain() {
		// Nothing to do.
	}

	/**
	 * Entry point.
	 *
	 * @param args the arguments
	 * @param inst the {@link Instrumentation instrumentation tools}
	 */
	public static void premain(final String args, final Instrumentation inst) {
		final FaultyAgentStarter starter = new FaultyAgentStarter(inst);
		starter.start(args);
	}

}
