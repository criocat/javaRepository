package expression.exceptions;

import java.math.BigDecimal;
import expression.*;

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
    public long calc(int num1, int num2) {
        return (long)num1 * (long)num2;
    }
}
