package expression;

import java.math.BigDecimal;

public class Divide extends AbstractOperation implements ExpressionPart {
    public Divide(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public String getOperation() {
        return "/";
    }

    public int getPrior() {
        return 1;
    }

    public int calcInt(int num1, int num2) {
        return num1 / num2;
    }

    public BigDecimal calcBigDecimal(BigDecimal num1, BigDecimal num2) {
        return num1.divide(num2);
    }
}
