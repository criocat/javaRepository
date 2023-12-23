package expression.exceptions;

import java.math.BigDecimal;
import expression.*;


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

    public long calc(int num1, int num2) {
        return (long)num1 / (long)num2;
    }
}
