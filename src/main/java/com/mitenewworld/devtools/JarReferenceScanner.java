package com.mitenewworld.devtools;
import com.mitenewworld.MITENewWorld;

import org.objectweb.asm.*;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.jar.JarFile;



public class JarReferenceScanner {

    public static void main(String[] args) throws Exception {


        String jarPath = "G:\\mc\\HMCL\\.minecraft\\versions\\Vault Hunters Official Pack zh (3rd Ed.)\\mods\\the_vault-1.18.2-3.20.3.6055.jar";
        String outputFile = "G:\\mc\\the_vault_fixed\\src\\main\\java\\org\\mainfixed\\the_vault_fixed\\output.txt"; // 确认路径


        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            JarFile jar = new JarFile(jarPath);
            jar.stream().filter(e -> e.getName().endsWith(".class")).forEach(entry -> {
                try (InputStream in = jar.getInputStream(entry)) {
                    ClassReader reader = new ClassReader(in);
                    reader.accept(new ClassVisitor(Opcodes.ASM9) {
                        String className;

                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            this.className = name;
                            super.visit(version, access, name, signature, superName, interfaces);
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            return new MethodVisitor(Opcodes.ASM9) {
                                @Override
                                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                    String msg = String.format("Field access: %s.%s -> %s %s", className, name, owner, descriptor);
                                    System.out.println(msg);
                                    writer.println(msg);
                                }

                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    String msg = String.format("Method call: %s -> %s.%s%s", className, owner, name, descriptor);
                                    System.out.println(msg);
                                    writer.println(msg);
                                }

                                @Override
                                public void visitTypeInsn(int opcode, String type) {
                                    String msg = String.format("Class usage: %s -> %s (opcode %d)", className, type, opcode);
                                    System.out.println(msg);
                                    writer.println(msg);
                                }
                            };
                        }
                    }, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Scan finished, output saved to: " + outputFile);
        }
    }
}
