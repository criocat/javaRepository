package expression.exceptions;

import expression.ExpressionPart;


public class CheckedDivide extends AbstractCheckedOperation implements ExpressionPart {
    public CheckedDivide(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public String getOperation() {
        return "/";
    }

    public int getPrior() {
        return 1;
    }

    public int calc(int num1, int num2) {
        if (num1 == Integer.MIN_VALUE && num2 == -1) {
            throw new RuntimeException("overflow");
        }
        if (num2 == 0) {
            throw new RuntimeException("division by zero");
        }
        return num1 / num2;
    }
}
