package expression;

public interface ExpressionPart extends ToMiniString, Expression, TripleExpression, ListExpression {
    String toString();

    void toString(StringBuilder strBuilder);
    void toMiniString(StringBuilder strBuilder);
    boolean equals(Object obj);
    boolean equals(ExpressionPart l, ExpressionPart r);
    int hashCode();

    int getPrior();
}
