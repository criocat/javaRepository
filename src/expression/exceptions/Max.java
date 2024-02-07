package expression.exceptions;

import expression.ExpressionPart;

public class Max extends AbstractCheckedOperation implements ExpressionPart {
    public Max(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 8;
    }

    public String getOperation() {
        return "max";
    }

    public int calc(int num1, int num2) {
        return Math.max(num1, num2);
    }

}
