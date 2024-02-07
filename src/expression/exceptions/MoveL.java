package expression.exceptions;

import expression.ExpressionPart;

public class MoveL extends AbstractCheckedOperation implements ExpressionPart {
    public MoveL(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 5;
    }

    public String getOperation() {
        return "<<";
    }

    public int calc(int num1, int num2) {
        return num1 << num2;
    }

}
