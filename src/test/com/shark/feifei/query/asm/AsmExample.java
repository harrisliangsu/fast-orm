package com.shark.feifei.query.asm;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AsmExample extends ClassLoader implements Opcodes {

	public static class Foo {
		public static void execute() {
			System.out.println("test changed method name");
		}

		public static void changeMethodContent() {
			System.out.println("test change method");
		}
	}

	public static void main(String[] args) throws IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException {

		ClassReader cr = new ClassReader(Foo.class.getName());
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
		ClassVisitor cv = new MethodChangeClassAdapter(cw);
		cr.accept(cv, Opcodes.ASM4);

		//新增加一个方法
		MethodVisitor mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
				"add",
				"([Ljava/lang/String;)V",
				null,
				null);
		// pushes the 'out' field (of type PrintStream) of the System class
		mw.visitFieldInsn(GETSTATIC,
				"java/lang/System",
				"out",
				"Ljava/io/PrintStream;");
		// pushes the "Hello World!" String constant
		mw.visitLdcInsn("this is add method print!");
		// invokes the 'println' method (defined in the PrintStream class)
		mw.visitMethodInsn(INVOKEVIRTUAL,
				"java/io/PrintStream",
				"println",
				"(Ljava/lang/String;)V");
		mw.visitInsn(RETURN);
		// this code uses a maximum of two stack elements and two local
		// variables
		mw.visitMaxs(0, 0);
		mw.visitEnd();

		// gets the bytecode of the Example class, and loads it dynamically
		byte[] code = cw.toByteArray();


		AsmExample loader = new AsmExample();
		Class<?> exampleClass = loader.defineClass(Foo.class.getName(), code, 0, code.length);

		for (Method method : exampleClass.getMethods()) {
			System.out.println(method);
		}

		System.out.println("*************");


		// uses the dynamically generated class to print 'Helloworld'
		//exampleClass.getMethods()[0].invoke(null,null);  //調用changeMethodContent，修改方法內容

		System.out.println("*************");


		exampleClass.getMethods()[1].invoke(null, null); //調用execute,修改方法名

		// gets the bytecode of the Example class, and loads it dynamically

		File file=new File("e:\\Example.class");
		if (!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(code);
		fos.close();
	}

	static class MethodChangeClassAdapter extends ClassVisitor implements Opcodes {

		public MethodChangeClassAdapter(final ClassVisitor cv) {
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
			if (cv != null && "execute".equals(name)) { //当方法名为execute时，修改方法名为execute1
				return cv.visitMethod(access, name + "1", desc, signature, exceptions);
			}

			if (cv != null && "changeMethodContent".equals(name))  //此处的changeMethodContent即为需要修改的方法  ，修改方法內容
			{
				MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);//先得到原始的方法
				MethodVisitor newMethod = new AsmMethodVisit(mv); //访问需要修改的方法
				return newMethod;
			}

			if (cv != null) {
				return cv.visitMethod(access, name, desc, signature, exceptions);
			}

			return null;
		}


	}

	static class AsmMethodVisit extends MethodVisitor {

		public AsmMethodVisit(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			super.visitMethodInsn(opcode, owner, name, desc);
		}

		@Override
		public void visitCode() {
			//此方法在访问方法的头部时被访问到，仅被访问一次
			//此处可插入新的指令
			super.visitCode();
		}

		@Override
		public void visitInsn(int opcode) {
			//此方法可以获取方法中每一条指令的操作类型，被访问多次
			//如应在方法结尾处添加新指令，则应判断：
			if (opcode == Opcodes.RETURN) {
				// pushes the 'out' field (of type PrintStream) of the System class
				mv.visitFieldInsn(GETSTATIC,
						"java/lang/System",
						"out",
						"Ljava/io/PrintStream;");
				// pushes the "Hello World!" String constant
				mv.visitLdcInsn("this is a modify method!");
				// invokes the 'println' method (defined in the PrintStream class)
				mv.visitMethodInsn(INVOKEVIRTUAL,
						"java/io/PrintStream",
						"println",
						"(Ljava/lang/String;)V");
//                mv.visitInsn(RETURN);
			}
			super.visitInsn(opcode);
		}
	}

}