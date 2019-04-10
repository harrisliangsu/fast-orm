package com.shark.feifei.query.asm;

import com.shark.feifei.db.transaction.TransactionUtil;
import com.shark.util.asm.TypeClassVisitor;
import com.shark.util.asm.TypeMethodVisitor;
import org.objectweb.asm.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/13 0013
 */
public class AsmQuickTest extends ClassLoader implements Opcodes {

	public static void main(String[] args) throws NoSuchMethodException {
		System.out.println(Type.getType(TransactionUtil.class).getInternalName());
		Method method=TransactionUtil.class.getMethod("sessionCommit");
		System.out.println(Type.getType(method).getInternalName());
	}

	@Test
	public void testTypeAsm() throws IOException, NoSuchMethodException {
		TypeClassVisitor tcv=TypeClassVisitor.asm4(TextExample.class);
		Method speak=TextExample.class.getMethod("speak");
		TypeMethodVisitor tmv=new TypeMethodVisitor(ASM4) {
			@Override
			public void preMethod() {
				// null
			}

			@Override
			public void postMethod() {
				visitMethodInsn(INVOKESTATIC, TransactionUtil.class, "sessionCommit", false);
			}
		};
		tcv.putEnhance(speak, tmv).accept();

		byte[] code=tcv.classByteData();
		AsmQuickTest loader = new AsmQuickTest();
		Class testClass= loader.defineClass(TextExample.class.getName(), code, 0, code.length);

		File file=new File("e:\\TextExample.class");
		if (!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(code);
		fos.close();
	}

	@Test
	public void testAsm() throws IOException {
		ClassReader cr = new ClassReader(TextExample.class.getName());
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
		ClassVisitor cv = new MethodModifyClassAdapter(cw);
		cr.accept(cv, Opcodes.ASM4);

		// gets the bytecode of the Example class, and loads it dynamically
		byte[] code = cw.toByteArray();

		AsmQuickTest loader = new AsmQuickTest();
		Class<?> exampleClass = loader.defineClass(TextExample.class.getName(), code, 0, code.length);

		for (Method method : exampleClass.getMethods()) {
			System.out.println(method);
		}

		File file=new File("e:\\TextExample.class");
		if (!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(code);
		fos.close();
	}

	static class MethodModifyClassAdapter extends ClassVisitor implements Opcodes {

		public MethodModifyClassAdapter(final ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
		}

		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			if (cv != null) {
				cv.visit(version, access, name, signature, superName, interfaces);
			}
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (cv != null && "speak".equals(name))  //此处的changeMethodContent即为需要修改的方法  ，修改方法內容
			{
				MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);//先得到原始的方法
				MethodVisitor newMethod = new AsmQuickTest.AsmMethodVisit(mv); //访问需要修改的方法
				return newMethod;
			}

			if (cv != null) {
				return cv.visitMethod(access, name, desc, signature, exceptions);
			}

			return null;
		}


	}

	static class AsmMethodVisit extends MethodVisitor implements Opcodes{

		public AsmMethodVisit(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			super.visitMethodInsn(opcode, owner, name, desc);
		}

		@Override
		public void visitCode() {
			// 前置增强
			super.visitCode();
		}

		@Override
		public void visitInsn(int opcode) {
			//此方法可以获取方法中每一条指令的操作类型，被访问多次
			//如应在方法结尾处添加新指令，则应判断：
			if (opcode == Opcodes.RETURN) {
				// pushes the 'out' field (of type PrintStream) of the System class
				mv.visitMethodInsn(
						INVOKESTATIC,
						Type.getType(TransactionUtil.class).getClassName(),
						"sessionCommit",
						"()V");
			}
			super.visitInsn(opcode);
		}
	}
}
