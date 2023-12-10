package expression;

import java.math.BigDecimal;

public class Const implements ExpressionPart {
    private final int val;
    private final BigDecimal decVal;
    private final int hashCode;
    public Const(int val) {
        this.val = val;
        decVal = BigDecimal.valueOf(val);
        hashCode = decVal.hashCode();
    }
    public Const(BigDecimal decVal) {
        this.decVal = decVal;
        val = decVal.intValue();
        hashCode = decVal.hashCode();
    }
    public int evaluate(int x) {
        return val;
    }
    public BigDecimal evaluate(BigDecimal x) {
        return decVal;
    }
    public int evaluate(int x, int y, int z) {
        return val;
    }
    public String toString() {
        return decVal.toString();
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(decVal.toString());
    }
    public void toMiniString(StringBuilder res) {
        res.append(decVal.toString());
    }
    public String toMiniString() {
        return decVal.toString();
    }
    public int getPrior() {
        return 0;
    }
    public int hashCode() {
        return hashCode;
    }
    public boolean equals(Object object) {
        if (object != null && object.getClass() == Const.class) {
            return evaluate(new BigDecimal(0)).equals(((Const) object).evaluate(new BigDecimal(0)));
        }
        return false;
    }
    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l == null && r == null;
    }
}
