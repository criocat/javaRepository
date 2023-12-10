package expression;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class AbstractOperation {
    private ArrayList<ExpressionPart> parts = new ArrayList<>();
    private final int prior;
    private final int hashCode;
    private final String operationStr;

    public AbstractOperation(ExpressionPart pl, ExpressionPart pr, String operationStr, int prior) {
        parts.add(pl);
        parts.add(pr);
        this.operationStr = operationStr;
        this.prior = prior;
        long hash1 = parts.get(0).hashCode();
        long hash2 = parts.get(1).hashCode();
        int mod = 1000000007, p1 = 153, p2 = 293;
        hashCode = (int) ((hash1 * (p1 + 17 * prior) + hash2 * (p2 + 29 * prior)) % mod);
    }

    protected IntPair getResult(int x) {
        return new IntPair(parts.get(0).evaluate(x), parts.get(1).evaluate(x));
    }

    protected BigDecimalPair getDecResult(BigDecimal x) {
        return new BigDecimalPair(parts.get(0).evaluate(x), parts.get(1).evaluate(x));
    }

    protected IntPair getResult(int x, int y, int z) {
        return new IntPair(parts.get(0).evaluate(x, y, z), parts.get(1).evaluate(x, y, z));
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        toString(strBuilder);
        return strBuilder.toString();
    }

    public void toString(StringBuilder strBuilder) {
        strBuilder.append('(');
        parts.get(0).toString(strBuilder);
        strBuilder.append(" " + operationStr + " ");
        parts.get(1).toString(strBuilder);
        strBuilder.append(')');
    }

    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res);
        return res.toString();
    }

    public void toMiniString(StringBuilder res) {
        int priorL = parts.get(0).getPrior();
        int priorR = parts.get(1).getPrior();
        boolean bracketsL = false;
        boolean bracketsR = false;
        if (prior == 4 && priorR >= 3) {
            bracketsR = true;
        }
        if (prior <= 2) {
            if (priorL >= 3) {
                bracketsL = true;
            }
            if (priorR >= 3 || prior == 2 && priorR == 1 || prior == 1) {
                bracketsR = true;
            }
        }
        bracketsL = bracketsL && (priorL != 0);
        bracketsR = bracketsR && (priorR != 0);
        res.append((bracketsL ? "(" : ""));
        parts.get(0).toMiniString(res);
        res.append((bracketsL ? ")" : "")).append(" ").append(operationStr).append(" ").append((bracketsR ? "(" : ""));
        parts.get(1).toMiniString(res);
        res.append((bracketsR ? ")" : ""));
    }

    public int getPrior() {
        return prior;
    }

    public boolean equals(Object object) {
        if (object != null && object.getClass() == this.getClass()) {
            if (((ExpressionPart) object).hashCode() != hashCode()) {
                return false;
            }
            return ((ExpressionPart) object).equals(parts.get(0), parts.get(1));
        }
        return false;
    }

    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l != null && r != null && l.equals(parts.get(0)) && r.equals(parts.get(1));
    }

    public int hashCode() {
        return hashCode;
    }
}
