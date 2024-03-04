package expression.generic;

public class ValuePair<T> {
    private final T first, second;
    public ValuePair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }
    public T getSecond() {
        return second;
    }
}
