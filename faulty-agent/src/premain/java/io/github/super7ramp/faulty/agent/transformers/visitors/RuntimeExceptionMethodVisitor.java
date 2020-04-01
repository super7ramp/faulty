package io.github.super7ramp.faulty.agent.transformers.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import io.github.super7ramp.faulty.api.InjectedRuntimeException;

/**
 * Transform method to throw an {@link InjectedRuntimeException}.
 */
public final class RuntimeExceptionMethodVisitor extends MethodVisitor {

	public RuntimeExceptionMethodVisitor(final int api, final MethodVisitor methodVisitor) {
		super(api, methodVisitor);
	}

	@Override
	public final void visitCode() {
		final String exceptionName = InjectedRuntimeException.class.getName().replace('.', '/');
		visitTypeInsn(Opcodes.NEW, exceptionName);
		visitInsn(Opcodes.DUP);
		visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionName, "<init>", "()V", false);
		visitInsn(Opcodes.ATHROW);
		visitEnd();
	}

}