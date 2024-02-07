package expression.exceptions;

import expression.ExpressionPart;

public class CheckedNegate extends AbstractCheckedUnar implements ExpressionPart {
    public CheckedNegate(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "-";
    }
    public int calc(int num) {
        if (num == Integer.MIN_VALUE) {
            throw new RuntimeException("overflow");
        }
        return -num;
    }
}
