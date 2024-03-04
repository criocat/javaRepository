package expression.generic;

public abstract class AbstractNumberOperations<T> {
    public abstract T add(T x, T y) throws RuntimeException;

    public abstract T sub(T x, T y) throws RuntimeException;

    public abstract T mul(T x, T y) throws RuntimeException;

    public abstract T div(T x, T y) throws RuntimeException;

    public abstract T negate(T x) throws RuntimeException;

    public abstract T convertInt(int x);
}