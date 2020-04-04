package io.github.super7ramp.faulty.agent.transformers.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import io.github.super7ramp.faulty.api.InjectedRuntimeException;

/**
 * Transform method to throw an {@link InjectedRuntimeException}.
 */
public final class RuntimeExceptionMethodVisitor extends MethodVisitor {

	/**
	 * Constructor.
	 * 
	 * @param api           the ASM API version
	 * @param methodVisitor the delegate method visitor
	 */
	public RuntimeExceptionMethodVisitor(final int api, final MethodVisitor methodVisitor) {
		super(api, methodVisitor);
	}

	@Override
	public final void visitCode() {
		final String exceptionName = Type.getInternalName(InjectedRuntimeException.class);
		visitTypeInsn(Opcodes.NEW, exceptionName);
		visitInsn(Opcodes.DUP);
		visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE),
				false);
		visitInsn(Opcodes.ATHROW);
		visitEnd();
	}

}