package expression.exceptions;

import java.math.BigDecimal;
import expression.*;

public class Min extends AbstractCheckedOperation implements ExpressionPart {
    public Min(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 6;
    }

    public String getOperation() {
        return "min";
    }

    public long calc(int num1, int num2) {
        return Math.min((long)num1, (long)num2);
    }

}
