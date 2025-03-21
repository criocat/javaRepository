package info.kgeorgiy.ja.razinkov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.full.classes.standard.BMPImageWriteParam;
import info.kgeorgiy.java.advanced.implementor.tools.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class that contains method
 * {@link Implementor#implement(Class, Path)}
 * required for code generation of simple classes, that implements some class or interface.
 *
 * @author Razinkov Ivan
 */
public class Implementor implements JarImpler {

    /**
     * Creates instance of {@link Implementor}
     */
    public Implementor() {}

    /**
     * Writes 4 spaces in {@code out} stream {@code repeat} times.
     *
     * @param out output stream for writing
     * @param repeat count of repeats of writing spaces
     * @throws IOException if {@code out} throws exception during text output
     */
    private void writeSpaces(BufferedWriter out, int repeat) throws IOException {
        for (int i = 0; i < repeat; i++) {
            out.write("    ");
        }
    }

    /**
     * Outputs sequence of names of variables separated by comma and enclosed in round brackets.
     * Names of variables are v{i}, where {i} is position of variable in {@code params}.
     *
     * @param out output stream for writing
     * @param params array of classes, which will be written in {@code out} stream
     * @param writeValueTypes if set to {@code true} then also output types of variables in {@code params}
     * @throws IOException if {@code out} throws exception during text output
     */
    private void writeParameters(BufferedWriter out, Class<?>[] params, boolean writeValueTypes) throws IOException {
        out.write("(");
        for (int pos = 0; pos < params.length; pos++) {
            Class<?> param = params[pos];
            if (writeValueTypes) out.write(param.getCanonicalName());
            if (writeValueTypes || pos != 0) out.write(" ");
            out.write("v" + pos);
            if (pos != params.length - 1) {
                out.write(", ");
            }
        }
        out.write(")");
    }

    /**
     * Returns string that represents default value for {@code clazz}.
     *
     * @param clazz class for which default value will be return
     * @return string containing default value for {@code clazz}
     */
    private String getDefaultValue(Class<?> clazz) {
        if (clazz == boolean.class) return "false";
        else if (clazz == void.class) return "";
        else if (clazz.isPrimitive()) return "0";
        else return "null";
    }

    /**
     * Returns string representation of some alternatives of {@link AccessRestriction} enum.
     *
     * @param access value that will be converted to string.
     * @throws RuntimeException if {@code access} doesn't hava string representation.
     * @return string representation of {@code access}.
     * @see AccessRestriction
     */
    private String getAccessString(AccessRestriction access) {
        return switch (access) {
            case PUBLIC -> "public";
            case PROTECTED -> "protected";
            case PACKAGE -> "";
            default -> throw new RuntimeException("Implementor: unexpected state");
        };
    }

    /**
     * Write overridden method corresponding to {@code method} and {@code accessString} parts of signature.
     * This method returns default value. Write method to {@code out}.
     *
     * @param out output stream for writing
     * @param method {@link MethodSignature} that represents methode to write
     * @param accessString String to write before function's return type
     * @throws IOException if {@code out} throws exception during text output
     * @see MethodSignature
     */
    private void writeMethod(BufferedWriter out, MethodSignature method, String accessString) throws IOException {
        writeSpaces(out, 1);
        out.write("@Override\n");
        writeSpaces(out, 1);
        out.write(accessString + " ");
        out.write(method.returnType().getCanonicalName() + " ");
        out.write(method.name());
        writeParameters(out, method.parameterTypes(), true);
        out.write(" {\n");
        writeSpaces(out, 2);
        out.write("return " + getDefaultValue(method.returnType()) + ";\n");
        writeSpaces(out, 1);
        out.write("}\n\n");
    }

    /**
     * Check that given class is not private. If it is private throws exception.
     *
     * @param clazz the class to check
     * @throws ImplerException if given class is private
     */
    private void checkParameterTypes(Class<?> clazz) throws ImplerException {
        if (Modifier.isPrivate(clazz.getModifiers())) {
            throw new ImplerException("Class " + clazz.getCanonicalName() + " is private");
        }
    }

    /**
     * Check that given array of classes doesn't contain private classes. If it contains then exception will be thrown.
     *
     * @param classes array of classes to check
     * @throws ImplerException if some classes in {@code classes} are private
     */
    private void checkParameterTypes(Class<?>[] classes) throws ImplerException {
        for (Class<?> clazz : classes) {
            checkParameterTypes(clazz);
        }
    }

    /**
     * Write constructor in {@code out} stream for class which extends another class that contains
     * constructor corresponding to {@code constructor} value.
     *
     * @param out output stream for writing
     * @param constructor {@link Constructor} that will be written
     * @throws IOException if {@code out} throws exception during text output
     * @throws ImplerException if some parameter types of constructor are private
     */
    private void writeConstructor(BufferedWriter out, Constructor<?> constructor) throws IOException, ImplerException {
        checkParameterTypes(constructor.getParameterTypes());
        writeSpaces(out, 1);
        out.write("public " + constructor.getDeclaringClass().getSimpleName() + "Impl");
        writeParameters(out, constructor.getParameterTypes(), true);
        if (constructor.getExceptionTypes().length > 0) {
            out.write(" throws ");
            out.write(Arrays.stream(constructor.getExceptionTypes()).map(Class::getCanonicalName).collect(Collectors.joining(", ")));
        }
        out.write(" {\n");
        writeSpaces(out, 2);
        out.write("super");
        writeParameters(out, constructor.getParameterTypes(), false);
        out.write(";\n");
        writeSpaces(out, 1);
        out.write("}\n\n");
    }

    /**
     * Generate code for class that inherit from {@code token}. Constructor for generated class is similar to one of
     * non-private constructors of {@code token} class. Override methods if required.
     *
     * @param token class for inheritance
     * @param out output stream for writing
     * @throws IOException if {@code out} throws exception during text output
     * @throws ImplerException if signature of constructor or overridden methods in generate class contains private classes
     */
    private void implementClass(BufferedWriter out, Class<?> token) throws IOException, ImplerException {
        Constructor<?> constructor = getConstructor(token);
        String className = token.getSimpleName() + "Impl";
        out.write("public class " + className + " extends " + token.getCanonicalName() + " {\n");
        writeConstructor(out, constructor);
        for (Map.Entry<MethodSignature, AccessRestriction> method : getNotPublicAbstractMethods(token)) {
            writeMethod(out, method.getKey(), getAccessString(method.getValue()));
        }
        for (MethodSignature method : getPublicAbstractMethods(token)) {
            writeMethod(out, method, "public");
        }
        out.write("}\n");
    }

    /**
     * Looking for non-private constructors in {@code token}.
     * Throws an exception if {@code token} doesn't contain such constructor.
     *
     * @param token class for searching for a constructor
     * @return non-private constructor in {@code token}
     * @throws ImplerException if {@code token} doesn't contain non-private constructor
     */
    private static Constructor<?> getConstructor(Class<?> token) throws ImplerException {
        Constructor<?> constructor = null;
        for (Constructor<?> constr : token.getDeclaredConstructors()) {
            if (!Modifier.isPrivate(constr.getModifiers())) {
                boolean good = true;
                for (Class<?> construct_param : constr.getParameterTypes()) {
                    if (Modifier.isPrivate(construct_param.getModifiers())) {
                        good = false;
                        break;
                    }
                }
                if (!good) continue;
                constructor = constr;
                break;
            }
        }
        if (constructor == null) {
            throw new ImplerException("Implementor: cannot find any non-private constructor");
        }
        return constructor;
    }

    /**
     * Looking for all non-public abstract methods which have to be overridden to inherit from {@code token} class.
     *
     * @param token class for looking for methods
     * @return {@link ArrayList} of pairs. Every pair represents signature of non-public method that have to be overridden
     * @throws ImplerException if some of non-public methods that have to be overridden contain private classes in signature
     */
    private ArrayList<Map.Entry<MethodSignature, AccessRestriction>> getNotPublicAbstractMethods(Class<?> token) throws ImplerException {
        Class<?> parent = token;
        HashMap<MethodSignature, AccessRestriction> methodMap = new HashMap<>();
        while (parent != null) {
            for (Method method : parent.getDeclaredMethods()) {
                MethodSignature signature = new MethodSignature(method.getName(), method.getParameterTypes(), method.getReturnType());
                if (!methodMap.containsKey(signature)) {
                    AccessRestriction access = AccessRestrictionByMethod(method);
                    methodMap.put(signature, access);
                }
            }
            parent = parent.getSuperclass();
        }
        ArrayList<Map.Entry<MethodSignature, AccessRestriction>> result = new ArrayList<>();
        for (Map.Entry<MethodSignature, AccessRestriction> pair : methodMap.entrySet()) {
            if (pair.getValue() != AccessRestriction.OVERRIDDEN && pair.getValue() != AccessRestriction.PUBLIC) {
                checkParameterTypes(pair.getKey().parameterTypes());
                checkParameterTypes(pair.getKey().returnType());
                result.add(new java.util.AbstractMap.SimpleEntry<>(pair.getKey(), pair.getValue()));
            }
        }
        return result;
    }

    /**
     * Returns {@link AccessRestriction} corresponding to {@code method}.
     *
     * @param method method to get {@link AccessRestriction}
     * @return {@link AccessRestriction} for given method
     * @see AccessRestriction
     */
    private static AccessRestriction AccessRestrictionByMethod(Method method) {
        AccessRestriction access;
        if (!Modifier.isAbstract(method.getModifiers()) || method.isDefault()) access = AccessRestriction.OVERRIDDEN;
        else if (Modifier.isProtected(method.getModifiers())) access = AccessRestriction.PROTECTED;
        else if (Modifier.isPublic(method.getModifiers())) access = AccessRestriction.PUBLIC;
        else if (Modifier.isPrivate(method.getModifiers())) access = AccessRestriction.PRIVATE;
        else access = AccessRestriction.PACKAGE;
        return access;
    }

    /**
     * Looking for all public abstract methods which have to be overridden to inherit from {@code token} class.
     *
     * @param token class for looking for methods
     * @return {@link ArrayList} of public method's signature that have to be overridden
     * @throws ImplerException if some of public methods that have to be overridden contain private classes in signature
     */
    private ArrayList<MethodSignature> getPublicAbstractMethods(Class<?> token) throws ImplerException {
        ArrayList<MethodSignature> result = new ArrayList<>();
        for (Method method : token.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers()) && !method.isDefault()) {
                checkParameterTypes(method.getParameterTypes());
                checkParameterTypes(method.getReturnType());
                result.add(new MethodSignature(method.getName(), method.getParameterTypes(), method.getReturnType()));
            }
        }
        return result;
    }

    /**
     * Generate code for class that implements {@code token} interface. Override methods if required.
     *
     * @param out output stream for writing
     * @param token interface for implementation
     * @throws IOException if {@code out} throws exception during text output
     * @throws ImplerException if some signatures of methods that have to be overridden contain private classes
     */
    private void implementInterface(BufferedWriter out, Class<?> token) throws IOException, ImplerException {
        String className = token.getSimpleName() + "Impl";
        out.write("public class " + className + " implements " + token.getCanonicalName() + " {\n");
        for (MethodSignature method : getPublicAbstractMethods(token)) {
            writeMethod(out, method, "public");
        }
        out.write("}\n");
    }

    /**
     * Produces code generation of class or interface.
     * Generate class that extends class or implements interface represented by {@code token}.
     *
     * @param token class to create implementation for.
     * @param root directory for generated code.
     * @throws ImplerException if cannot write class to output file,
     * or it is impossible to write class what inherit from or implement {@code token} class.
     * @see Impler
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        checkArgs(token, root);
        String packageName = token.getPackageName().replace(".", File.separator);
        Path rootPath = Paths.get(System.getProperty("user.dir") + File.separator + root + File.separator + packageName);
        Path classPath = Paths.get(rootPath + File.separator + token.getSimpleName() + "Impl.java");
        try {
            Files.createDirectories(rootPath);
        } catch (IOException | InvalidPathException e) {
            throw new ImplerException("Implementor: cannot create output directory", e);
        }
        try (BufferedWriter out = Files.newBufferedWriter(classPath, StandardCharsets.US_ASCII)) {
            out.write("package " + token.getPackageName() + ";\n\n");
            if (token.isInterface()) {
                implementInterface(out, token);
            } else {
                implementClass(out, token);
            }
        } catch (IOException e) {
            throw new ImplerException("Implementor: cannot write in output file", e);
        }
    }

    /**
     * Check that given arguments are not {@code null}, and
     * it is possible to inherit from (implement) {@code token} class (interface).
     *
     * @param token class to check
     * @param root path to check
     * @throws ImplerException if some arguments are {@code null}, or if it is impossible to inherit from {@code token} class
     */
    private static void checkArgs(Class<?> token, Path root) throws ImplerException {
        if (token == null || root == null) {
            throw new ImplerException("Implementor: required non-null argument");
        }
        if (token == Enum.class || token == Record.class || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Implementor: cannot inherit from Enum or Record");
        }
        if (token.isSealed()) {
            throw new ImplerException("Implementor: cannot inherit from sealed class");
        }
        if (Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("Implementor: cannot inherit from final class");
        }
    }

    /**
     * Compiles java file corresponding {@code compile_class} path. Uses class-path corresponding to {@code token} class.
     * Throws exception if method cannot find compiler or compilation failed with an error.
     *
     * @param compile_class path to java class to compile
     * @param token class to get class-path from it for compilation
     * @throws ImplerException if method cannot find java compiler or compiler cannot compile file
     */
    public void compileClass(Path compile_class, Class<?> token) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Implementor: cannot get compiler");
        }
        String[] args = {"-encoding", "UTF-8", "-cp", classPathByToken(token).toString(), compile_class.toString()};
        int result = compiler.run(null, System.out, System.err, args);
        if (result != 0) {
            throw new ImplerException("Implementor: compilation failed");
        }
    }

    /**
     * Converts class to its class-file path.
     *
     * @param token class to get class-file path
     * @return path to class-file of given {@code token}
     * @throws ImplerException if it cannot get class-file path from {@code token}
     */
    private Path classPathByToken(Class<?> token) throws ImplerException {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new ImplerException("Implementor: error occurred during getting class path", e);
        }
    }

    /**
     * Produces code generation of class or interface.
     * Generate class that extends class or implements interface represented by {@code token} and also put it in jar
     * file that corresponding to {@code jarFile} path.
     *
     * @param token type token to create implementation for.
     * @param jarFile path to jar file with generated class
     * @throws ImplerException if method cannot generate class that inherits from {@code token} class or
     * cannot to compile generated file or cannot write output files
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        if (token == null || jarFile == null) {
            throw new ImplerException("Implementor: required non-null argument");
        }
        Path root = jarFile.getParent();
        if (root == null) {
            throw new ImplerException("Implementor: jarFile must be path to jar file");
        }
        implement(token, root);
        String packageName = token.getPackageName().replace(".", File.separator);
        Path sourceDir = Paths.get(System.getProperty("user.dir") + File.separator + root + File.separator + packageName);
        Path sourcePath = Paths.get(sourceDir + File.separator + token.getSimpleName() + "Impl.java");
        compileClass(sourcePath, token);
        Path relatedClassPath = Path.of(packageName + File.separator + token.getSimpleName() + "Impl.class");
        Path fullClassPath = Paths.get(sourceDir + File.separator + token.getSimpleName() + "Impl.class");
        try(JarOutputStream out = new JarOutputStream(Files.newOutputStream(jarFile))) {
            JarEntry jarEntry = new JarEntry(relatedClassPath.toString());
            out.putNextEntry(jarEntry);
            Files.copy(fullClassPath, out);
            out.closeEntry();
            Files.delete(fullClassPath);
        } catch (IOException e) {
            throw new ImplerException("Implementor: error occurred during input/output operations in jar file", e);
        }
    }

    /**
     * Receive 3 arguments: -jar, string representing some class or interface, output jar file.
     * Creates jar file with given path that contains class that extents(implements) class(interface) corresponding to
     * given name.
     *
     * @param args receive -jar className output.jar
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("invalid number of arguments, expected 3: -jar className output.jar");
            return;
        }
        for (String arg : args) {
            if (arg == null) {
                System.err.println("null argument, expected -jar className output.jar");
                return;
            }
        }
        Class<?> token;
        try {
            token = Class.forName(args[1]);
        } catch (ClassNotFoundException e) {
            System.err.println("cannot find given class");
            return;
        }
        try {
            new Implementor().implementJar(token, Path.of(args[2]));
        } catch(ImplerException e) {
            System.err.println("error occurred during implementation of given class: " + e.getMessage());
            return;
        }
    }
}
