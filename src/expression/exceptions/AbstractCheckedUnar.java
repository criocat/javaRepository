package expression.exceptions;

import expression.ExpressionPart;
import expression.IntPair;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractCheckedUnar {
    private ExpressionPart exp;

    public AbstractCheckedUnar(ExpressionPart exp) {
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

    public int hashCode() {
        int mod = 1000000007;
        return (int) ((long) exp.hashCode() + getOperation().hashCode()) % mod;
    }

    abstract protected int calc(int num);

    public int evaluate(int x) {
        return calc(exp.evaluate(x));
    }



    public BigDecimal evaluate(BigDecimal x) {
        return BigDecimal.valueOf(evaluate(x.intValue()));
    }

    abstract protected String getOperation();


    public int evaluate(int x, int y, int z) {
        return calc(exp.evaluate(x, y, z));
    }

    public int evaluate(List<Integer> variables) {
        return calc(exp.evaluate(variables));
    }

    public boolean equals(Object object) {
        if (object != null && object.getClass() == this.getClass()) {
            return exp.equals((ExpressionPart) object);
        }
        return false;
    }

    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l == null && r == null;
    }
}
