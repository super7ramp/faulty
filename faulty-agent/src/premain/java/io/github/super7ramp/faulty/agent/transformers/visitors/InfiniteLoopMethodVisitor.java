package io.github.super7ramp.faulty.agent.transformers.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import io.github.super7ramp.faulty.agent.transformers.rollback.ActiveLoops;
import io.github.super7ramp.faulty.agent.transformers.rollback.TransformationListener;

/**
 * Transform method into a loop whose exit condition is
 * {@link ActiveLoops#isPresent(String)}, basically it prepends the method code
 * with:
 * 
 * <pre>
 * 0  ldc <String "com.example.ClassName,methodName"> [2]
 * 2  invokestatic io.github.super7ramp.faulty.agent.transformers.ActiveLoops.isPresent(java.lang.String) : boolean [3]
 * 5  ifeq 11
 * 8  goto 0
 * </pre>
 */
public class InfiniteLoopMethodVisitor extends MethodVisitor {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(InfiniteLoopMethodVisitor.class.getName());

	/** Transformation listeners. */
	private final Collection<TransformationListener> listeners;

	/** Visited class name. */
	private final String className;

	/** Visited method name. */
	private final String methodName;

	/**
	 * Constructor.
	 * 
	 * @param api               the ASM API version
	 * @param mv                the method visitor to delegate visit calls to
	 * @param visitedClassName  the name of the visited class
	 * @param visitedMethodName the name of the visited method
	 */
	public InfiniteLoopMethodVisitor(final int api, final MethodVisitor mv, final String visitedClassName,
			final String visitedMethodName) {
		super(api, mv);
		listeners = new ArrayList<>();
		className = visitedClassName;
		methodName = visitedMethodName;
	}

	/**
	 * Add a {@link TransformationListener}.
	 * 
	 * @param listener listener to be notified when transformation occurs
	 * @return this {@link InfiniteLoopMethodVisitor}
	 */
	public InfiniteLoopMethodVisitor addListener(final TransformationListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public final void visitCode() {
		LOGGER.info("Adding non-interruptible infinite loop");
		final Label loopLabel = new Label();
		final Label outsideLoopLabel = new Label();

		// Loop while ActiveLoops.isPresent(loopIdentifier) returns true
		visitLabel(loopLabel);
		visitLdcInsn(ActiveLoops.idOf(className, methodName));
		visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(ActiveLoops.class), "isPresent",
				Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(String.class)), false);
		visitJumpInsn(Opcodes.IFEQ, outsideLoopLabel);
		visitJumpInsn(Opcodes.GOTO, loopLabel);

		visitLabel(outsideLoopLabel);

		// Add the rest of the original code
		mv.visitCode();

		// Notify listeners that transformation occurred
		listeners.forEach(listener -> listener.notifyTransformation(className, methodName));
	}
}