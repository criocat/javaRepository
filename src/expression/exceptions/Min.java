package expression.exceptions;

import expression.ExpressionPart;

public class Min extends AbstractCheckedOperation implements ExpressionPart {
    public Min(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 9;
    }

    public String getOperation() {
        return "min";
    }

    public int calc(int num1, int num2) {
        return Math.min(num1, num2);
    }

}
