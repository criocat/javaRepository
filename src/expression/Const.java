package expression;

import java.math.BigDecimal;
import java.util.List;

public class Const implements ExpressionPart {
    private final BigDecimal val;
    public Const(int val) {
        this.val = BigDecimal.valueOf(val);
    }
    public Const(BigDecimal val) {
        this.val = val;
    }

    public int evaluate(int x) {
        return val.intValue();
    }
    public BigDecimal evaluate(BigDecimal x) {
        return val;
    }
    public int evaluate(int x, int y, int z) {
        return val.intValue();
    }
    public String toString() {
        return val.toString();
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(val.toString());
    }
    public void toMiniString(StringBuilder res) {
        res.append(val.toString());
    }
    public String toMiniString() {
        return val.toString();
    }
    public int getPrior() {
        return 0;
    }
    public int hashCode() {
        return val.hashCode();
    }

    public int evaluate(List<Integer> variables) {
        return val.intValue();
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
