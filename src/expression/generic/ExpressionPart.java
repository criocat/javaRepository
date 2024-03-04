package expression.generic;




public interface ExpressionPart<T> {
    String toString();
    String toMiniString();
    void toString(StringBuilder strBuilder);
    void toMiniString(StringBuilder strBuilder);

    T evaluate(T a, T b, T c) throws RuntimeException;
    T evaluate(T a) throws RuntimeException;
    int getPrior();
}
