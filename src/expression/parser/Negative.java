package expression.parser;

import expression.Const;
import expression.ExpressionPart;
import expression.Multiply;
import expression.Variable;

import java.math.BigDecimal;

public class Negative implements ExpressionPart {
    ExpressionPart val = null;
    private final int hashCode;
    public Negative(ExpressionPart val) {
        this.val = val;
        int mod = 1000000007;
        hashCode = (int)((long)val.hashCode() + 13) % mod;
    }
    public int evaluate(int x) {
        return -val.evaluate(x);
    }
    public BigDecimal evaluate(BigDecimal x) {
        return val.evaluate(x).multiply(BigDecimal.valueOf(-1));
    }
    public int evaluate(int x, int y, int z) {
        return -val.evaluate(x, y, z);
    }
    public String toString() {
        return "-(" + val.toString() + ")";
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append("-(");
        val.toString(strBuilder);
        strBuilder.append(")");
    }
    public void toMiniString(StringBuilder res) {
        res.append("");
    }
    public String toMiniString() {
        return "";
    }
    public int getPrior() {
        return 0;
    }
    public int hashCode() {
        return hashCode;
    }
    public boolean equals(Object object) {
        if (object != null && object.getClass() == Negative.class) {
            return val.equals((ExpressionPart) object);
        }
        return false;
    }
    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l == null && r == null;
    }
}
