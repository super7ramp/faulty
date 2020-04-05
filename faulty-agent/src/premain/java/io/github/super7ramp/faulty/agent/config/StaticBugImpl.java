package io.github.super7ramp.faulty.agent.config;

import java.util.Objects;

/**
 * Implementation of {@link StaticBug}.
 */
final class StaticBugImpl implements StaticBug {

	/** Class name. */
	private final String className;

	/** Method name. */
	private final String methodName;

	/** Kind. */
	private final Kind kind;

	/**
	 * Constructor.
	 * 
	 * @param aClassName  class name
	 * @param aMethodName method name
	 * @param aKind       kind
	 */
	StaticBugImpl(final String aClassName, final String aMethodName, final Kind aKind) {
		className = aClassName;
		methodName = aMethodName;
		kind = aKind;
	}

	@Override
	public final String className() {
		return className;
	}

	@Override
	public final String methodName() {
		return methodName;
	}

	@Override
	public final Kind kind() {
		return kind;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof StaticBugImpl)) {
			return false;
		}
		final StaticBugImpl other = (StaticBugImpl) obj;
		return kind == other.kind && Objects.equals(className, other.className)
				&& Objects.equals(methodName, other.methodName);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(className, methodName, kind);
	}

	@Override
	public final String toString() {
		return String.format("[kind = %s, class = %s, method = %s]", kind, className, methodName);
	}

}
