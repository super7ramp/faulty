package io.github.super7ramp.faulty.agent.transformers.rollback;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A memory of injected infinite loops. Accessed by both agent and injected
 * code.
 */
// TODO create proper memory for all bugs
public final class ActiveLoops {

	/**
	 * Contains all the method names in which an infinite loop has been injected and
	 * is still active.
	 */
	private static final Set<String> ACTIVE_LOOPS = Collections.synchronizedSet(new HashSet<>());

	/**
	 * Constructor.
	 */
	private ActiveLoops() {
		// nothing to do.
	}

	/**
	 * Add an active loop to memory
	 * 
	 * @param identifier loop identifier (i.e. location)
	 */
	static final void add(final String identifier) {
		/*
		 * add() and remove() are accessed from one thread. Concurrency is with
		 * isPresent() which is all called from instrumented code. So it's fine that
		 * this method is not atomic.
		 */
		if (ACTIVE_LOOPS.contains(identifier)) {
			// TODO raise checked exception
		}
		ACTIVE_LOOPS.add(identifier);
	}

	/**
	 * Remove an active loop from memory.
	 *
	 * @param identifier loop identifier (i.e. location)
	 */
	static final void remove(final String identifier) {
		ACTIVE_LOOPS.remove(identifier);
	}

	/**
	 * Check if given method in given class contains an active injected loop.
	 * 
	 * @param identifier identifier of the loop (location)
	 * @return <tt>true</tt> if method contains an active loop
	 */
	public static final boolean isPresent(final String identifier) {
		return ACTIVE_LOOPS.contains(identifier);
	}

	/**
	 * Create an ID from class name and method name.
	 * 
	 * @param className  class name
	 * @param methodName method name
	 * @return id
	 */
	public static String idOf(final String className, final String methodName) {
		return String.join(",", className, methodName);
	}

}
