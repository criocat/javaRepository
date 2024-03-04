package expression.generic;


public abstract class AbstractUnary<T> {
    private final ExpressionPart<T> exp;

    public AbstractUnary(ExpressionPart<T> exp) {
        this.exp = exp;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        toString(res);
        return res.toString();
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(getOperation()).append("(");
        exp.toString(strBuilder);
        strBuilder.append(")");
    }
    public void toMiniString(StringBuilder res) {
        int prior = exp.getPrior();
        if (prior == 0) {
            res.append(getOperation()).append(" ");
            exp.toMiniString(res);
        }
        else {
            res.append(getOperation()).append("(");
            exp.toMiniString(res);
            res.append(")");
        }
    }
    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res);
        return res.toString();
    }

    public int getPrior() {
        return 0;
    }

    abstract protected T calc(T num) throws RuntimeException;

    abstract protected String getOperation();

    public T evaluate(T x) {
        return calc(exp.evaluate(x));
    }

    public T evaluate(T x, T y, T z) {
        return calc(exp.evaluate(x, y, z));
    }
}
