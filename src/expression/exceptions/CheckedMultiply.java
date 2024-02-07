package expression.exceptions;

import expression.ExpressionPart;

public class CheckedMultiply extends AbstractCheckedOperation implements ExpressionPart {
    public CheckedMultiply(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 2;
    }

    public String getOperation() {
        return "*";
    }
    public int calc(int num1, int num2) {
        int res = num1 * num2;
        if (num1 != 0 && num2 != 0 && (res / num2 != num1 || res / num1 != num2)) {
            throw new RuntimeException("overflow");
        }
        return res;
    }
}
