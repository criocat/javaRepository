package expression.parser;

import expression.Const;
import expression.ExpressionPart;
import expression.Multiply;
import expression.Variable;

import java.math.BigDecimal;

public abstract class AbstractUnar {
    ExpressionPart val;

    public AbstractUnar(ExpressionPart val) {
        this.val = val;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        toString(res);
        return res.toString();
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(getOperation()).append("(");
        val.toString(strBuilder);
        strBuilder.append(")");
    }
    public void toMiniString(StringBuilder res) {
        int prior = val.getPrior();
        if (prior == 0) {
            res.append(getOperation()).append(" ");
            val.toMiniString(res);
        }
        else {
            res.append(getOperation()).append("(");
            val.toMiniString(res);
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
        return (int) ((long) val.hashCode() + getOperation().hashCode()) % mod;
    }

    abstract protected int evaluateByNum(int num);

    public int evaluate(int x) {
        return evaluateByNum(val.evaluate(x));
    }
    public BigDecimal evaluate(BigDecimal x) {
        return BigDecimal.valueOf(evaluate(x.intValue()));
    }

    abstract protected String getOperation();


    public int evaluate(int x, int y, int z) {
        return evaluateByNum(val.evaluate(x, y, z));
    }

    public boolean equals(Object object) {
        if (object != null && object.getClass() == this.getClass()) {
            return val.equals((ExpressionPart) object);
        }
        return false;
    }

    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l == null && r == null;
    }
}
