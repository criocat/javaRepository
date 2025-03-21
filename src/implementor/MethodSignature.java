package info.kgeorgiy.ja.razinkov.implementor;

/**
 * Record that represents signature of method.
 *
 * @param name name of method
 * @param parameterTypes types of parameters of method
 * @param returnType type of return value of method
 * @author Razinkov Ivan
 */
public record MethodSignature(String name, Class<?>[] parameterTypes, Class<?> returnType) {}
