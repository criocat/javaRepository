package expression.parser;

import expression.Const;
import expression.ExpressionPart;
import expression.Multiply;
import expression.Variable;

import java.math.BigDecimal;

public class Negative extends AbstractUnar implements ExpressionPart {
    public Negative(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "-";
    }
    public int evaluateByNum(int num) {
        return -num;
    }
}
