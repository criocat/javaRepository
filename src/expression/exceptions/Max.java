package expression.exceptions;

import java.math.BigDecimal;
import expression.*;

public class Max extends AbstractCheckedOperation implements ExpressionPart {
    public Max(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 5;
    }

    public String getOperation() {
        return "max";
    }

    public long calc(int num1, int num2) {
        return Math.max((long)num1, (long)num2);
    }

}
