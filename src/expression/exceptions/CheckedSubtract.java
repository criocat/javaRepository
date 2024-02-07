package expression.exceptions;

import expression.ExpressionPart;


public class CheckedSubtract extends AbstractCheckedOperation implements ExpressionPart {
    public CheckedSubtract(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 4;
    }

    public String getOperation() {
        return "-";
    }

    public int calc(int num1, int num2) {
        if ((num1 <= 0 && num2 >= 0) && ((Integer.MIN_VALUE - num1) > -num2)) {
            throw new RuntimeException("overflow");
        }
        if ((num1 >= 0 && num2 <= 0) && ((num1 - Integer.MAX_VALUE) > num2)) {
            throw new RuntimeException("overflow");
        }
        return num1 - num2;
    }
}
