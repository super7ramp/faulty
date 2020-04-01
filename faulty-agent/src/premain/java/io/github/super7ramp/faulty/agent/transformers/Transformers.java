package io.github.super7ramp.faulty.agent.transformers;

import java.lang.instrument.ClassFileTransformer;
import java.util.function.Predicate;

/**
 * Factory of {@link ClassFileTransformer}s.
 */
public final class Transformers {

	/** ASM API version. */
	// TODO read from conf
	private static final int API = 7;

	/**
	 * Constructor.
	 */
	private Transformers() {
		// nothing to do.
	}

	/**
	 * A dummy transformer that will just rewrites input class as is.
	 *
	 * @param transformableClassPredicate only class satisfying this predicate will
	 *                                    be transformed
	 * @return the transformer
	 */
	public static final RevertableClassFileTransformer dummyTransformer(
			final Predicate<String> transformableClassPredicate) {
		return new DummyTransformer(API, transformableClassPredicate);
	}

	/**
	 * A transformer that modify specified method in specified class to inject it an
	 * infinite loop.
	 * 
	 * @param transformableClassPredicate  the class name matching predicate
	 * @param transformableMethodPredicate the method name matching predicate
	 * @return the transformer
	 */
	public static final RevertableClassFileTransformer infiniteLoopTransformer(
			final Predicate<String> transformableClassPredicate, final Predicate<String> transformableMethodPredicate) {
		return new InfiniteLoopTransformer(API, transformableClassPredicate, transformableMethodPredicate);
	}

	/**
	 * A transformer that modify specified method in specified class to inject it an
	 * infinite loop. This infinite loop will check thread status so it can actually
	 * be broken by Thread.interrupt().
	 * 
	 * @param transformableClassPredicate  the class name matching predicate
	 * @param transformableMethodPredicate the method name matching predicate
	 * @return the transformer
	 */
	public static final RevertableClassFileTransformer interruptibleInfiniteLoopTransformer(
			final Predicate<String> transformableClassPredicate, final Predicate<String> transformableMethodPredicate) {
		return new InterruptibleInfiniteLoopTransformer(API, transformableClassPredicate, transformableMethodPredicate);
	}

	/**
	 * A transformer that modify specified method in specified class to inject it a
	 * throw of RuntimeException.
	 * 
	 * @param transformableClassPredicate  the class name matching predicate
	 * @param transformableMethodPredicate the method name matching predicate
	 * @return the transformer
	 */
	public static final RevertableClassFileTransformer runtimeExceptionTransformer(
			final Predicate<String> transformableClassPredicate, final Predicate<String> transformableMethodPredicate) {
		return new RuntimeExceptionTransformer(API, transformableClassPredicate, transformableMethodPredicate);
	}

}
