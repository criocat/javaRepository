package expression;

import java.math.BigDecimal;

public class Add extends expression.AbstractOperation implements ExpressionPart {
    public Add(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }

    public int getPrior() {
        return 3;
    }

    public String getOperation() {
        return "+";
    }

    public int calcInt(int num1, int num2) {
        return num1 + num2;
    }

    public BigDecimal calcBigDecimal(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }
}
