package expression.exceptions;

import expression.ExpressionPart;

public class CheckedNegate extends AbstractCheckedUnar implements ExpressionPart {
    public CheckedNegate(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "-";
    }
    public long evaluateByNum(int num) {
        return -((long)num);
    }
}
