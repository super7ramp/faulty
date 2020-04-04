package io.github.super7ramp.faulty.agent.transformers.visitors;

import java.util.logging.Logger;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import io.github.super7ramp.faulty.api.InjectedInterruptedException;

/**
 * Transform method into an interruptible infinite loop, basically it writes:
 * 
 * <pre>
 *    0  invokestatic java.lang.Thread.interrupted() : boolean [2]
 *    3  ifne 9
 *    6  goto 0
 *    9  new io.github.super7ramp.faulty.api.InjectedInterruptedException [3]
 *   12  dup
 *   13  invokespecial io.github.super7ramp.faulty.api.InjectedInterruptedException() [4]
 *   16  athrow
 * </pre>
 */
public class InterruptibleInfiniteLoopMethodVisitor extends MethodVisitor {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(InterruptibleInfiniteLoopMethodVisitor.class.getName());

	/** Interrupted exception name. */
	private static final String INTERRUPTED_EXCEPTION_NAME = Type.getInternalName(InjectedInterruptedException.class);

	/**
	 * Constructor.
	 * 
	 * @param api the ASM API version
	 * @param mv  the method visitor to delegate visit calls to
	 */
	public InterruptibleInfiniteLoopMethodVisitor(final int api, final MethodVisitor mv) {
		super(api, mv);
	}

	@Override
	public final void visitCode() {

		LOGGER.info("Adding interruptible infinite loop");
		final Label loopLabel = new Label();
		final Label outsideLoopLabel = new Label();

		visitLabel(loopLabel);
		visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Thread.class), "interrupted",
				Type.getMethodDescriptor(Type.BOOLEAN_TYPE), false);
		visitJumpInsn(Opcodes.IFNE, outsideLoopLabel);
		visitJumpInsn(Opcodes.GOTO, loopLabel);

		visitLabel(outsideLoopLabel);
		visitTypeInsn(Opcodes.NEW, INTERRUPTED_EXCEPTION_NAME);
		visitInsn(Opcodes.DUP);
		visitMethodInsn(Opcodes.INVOKESPECIAL, INTERRUPTED_EXCEPTION_NAME, "<init>",
				Type.getMethodDescriptor(Type.VOID_TYPE), false);
		visitInsn(Opcodes.ATHROW);

		// Add the rest of the original code
		mv.visitCode();
	}
}