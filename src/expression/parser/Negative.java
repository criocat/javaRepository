package expression.parser;

import expression.ExpressionPart;

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
