package expression.exceptions;

import expression.ExpressionPart;
import expression.IntPair;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractCheckedOperation {
    private final ExpressionPart el, er;

    public AbstractCheckedOperation(ExpressionPart el, ExpressionPart er) {
        this.el = el;
        this.er = er;
    }

    protected IntPair getResult(int x) {
        return new IntPair(el.evaluate(x), er.evaluate(x));
    }

    protected IntPair getResultByList(List<Integer> l) {
        return new IntPair(el.evaluate(l), er.evaluate(l));
    }



    protected IntPair getResult(int x, int y, int z) {
        return new IntPair(el.evaluate(x, y, z), er.evaluate(x, y, z));
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

    abstract protected int calc(int num1, int num2);

    public int evaluate(int x) {
        IntPair res = getResult(x);
        return calc(res.getFirst(), res.getSecond());
    }

    public int evaluate(int x, int y, int z) {
        IntPair res = getResult(x, y, z);
        return calc(res.getFirst(), res.getSecond());
    }

    public int evaluate(List<Integer> variables) {
        IntPair res = getResultByList(variables);
        return calc(res.getFirst(), res.getSecond());
    }

    public boolean equals(Object object) {
        if (object != null && object.getClass() == this.getClass()) {
            return ((ExpressionPart) object).equals(el, er);
        }
        return false;
    }

    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l != null && r != null && l.equals(el) && r.equals(er);
    }

    public int hashCode() {
        long hash1 = el.hashCode();
        long hash2 = er.hashCode();
        int prior = getPrior();
        int mod = 1000000007, p1 = 153, p2 = 293;
        return (int) ((hash1 * (p1 + 17 * prior) + hash2 * (p2 + 29 * prior)) % mod);
    }
}
