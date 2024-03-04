package expression.generic;


public abstract class AbstractOperation<T> {
    private final ExpressionPart<T> el, er;

    public AbstractOperation(ExpressionPart<T> el, ExpressionPart<T> er) {
        this.el = el;
        this.er = er;
    }

    protected ValuePair<T> getResult(T x) {
        return new ValuePair<T>(el.evaluate(x), er.evaluate(x));
    }


    protected ValuePair<T> getResult(T x, T y, T z) {
        return new ValuePair<T>(el.evaluate(x, y, z), er.evaluate(x, y, z));
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        toString(strBuilder);
        return strBuilder.toString();
    }

    public void toString(StringBuilder strBuilder) {
        strBuilder.append('(');
        el.toString(strBuilder);
        strBuilder.append(" " + getOperation() + " ");
        er.toString(strBuilder);
        strBuilder.append(')');
    }


    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res);
        return res.toString();
    }

    public void toMiniString(StringBuilder res) {
        int priorL = el.getPrior();
        int priorR = er.getPrior();
        int prior = getPrior();
        boolean bracketsL = ((prior <= 2) && (priorL >= 3 && priorL <= 4) || (priorL >= 5 && (priorL > prior && prior < 5)));
        boolean bracketsR = (prior == 4 && priorR >= 3) || (priorR >= 5 && (priorR <= 7 || priorR != prior));
        if (prior <= 2 && (priorR >= 3 || prior == 2 && priorR == 1 || prior == 1)) {
            bracketsR = true;
        }
        bracketsR = bracketsR && (priorR != 0);
        res.append((bracketsL ? "(" : ""));
        el.toMiniString(res);
        res.append((bracketsL ? ")" : "")).append(" ").append(getOperation()).append(" ").append((bracketsR ? "(" : ""));
        er.toMiniString(res);
        res.append((bracketsR ? ")" : ""));
    }

    abstract public int getPrior();

    abstract  protected String getOperation();

    abstract protected T calc(T x, T y) throws RuntimeException;

    public T evaluate(T x) {
        ValuePair<T> res = getResult(x);
        return calc(res.getFirst(), res.getSecond());
    }

    public T evaluate(T x, T y, T z) {
        ValuePair<T> res = getResult(x, y, z);
        return calc(res.getFirst(), res.getSecond());
    }
}
