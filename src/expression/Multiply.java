package expression;

import java.math.BigDecimal;

public class Multiply extends AbstractOperation implements ExpressionPart {
    public Multiply(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 2;
    }

    public String getOperation() {
        return "*";
    }
    public int calcInt(int num1, int num2) {
        return num1 * num2;
    }

    public BigDecimal calcBigDecimal(BigDecimal num1, BigDecimal num2) {
        return num1.multiply(num2);
    }
}
