package io.github.super7ramp.faulty.agent.transformers.visitors;

import java.util.logging.Logger;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Transform method into an infinite loop, basically it writes:
 * 
 * <pre>
 * 0: goto 0
 * </pre>
 */
public class InfiniteLoopMethodVisitor extends MethodVisitor {

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(InfiniteLoopMethodVisitor.class.getName());

	/**
	 * Constructor.
	 * 
	 * @param api the ASM API version
	 * @param mv  the method visitor to delegate visit calls to
	 */
	public InfiniteLoopMethodVisitor(final int api, final MethodVisitor mv) {
		super(api, mv);
	}

	@Override
	public final void visitCode() {
		LOGGER.info("Adding non-interruptible infinite loop");
		final Label firstLine = new Label();
		visitLabel(firstLine);
		visitJumpInsn(Opcodes.GOTO, firstLine);
		visitEnd();
	}
}