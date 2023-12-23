package expression.parser;

import expression.AbstractOperation;
import expression.ExpressionPart;

import java.math.BigDecimal;

public class And extends AbstractOperation implements ExpressionPart {
    public And(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2);
    }
    public int getPrior() {
        return 5;
    }
    public String getOperation() {
        return "&";
    }
    public int calcInt(int num1, int num2) {
        return num1 & num2;
    }
    public BigDecimal calcBigDecimal(BigDecimal num1, BigDecimal num2) {
        return BigDecimal.valueOf(num1.intValue() &  num2.intValue());
    }
}
