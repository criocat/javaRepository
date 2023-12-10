package expression;

import java.math.BigDecimal;

public class Multiply extends AbstractOperation implements ExpressionPart {
    public Multiply(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2, "*", 2);
    }
    public int evaluate(int x) {
        IntPair res = super.getResult(x);
        return res.getFirst() * res.getSecond();
    }
    public BigDecimal evaluate(BigDecimal x) {
        BigDecimalPair res = super.getDecResult(x);
        return res.getFirst().multiply((res.getSecond()));
    }
    public int evaluate(int x, int y, int z) {
        IntPair res = super.getResult(x, y, z);
        return res.getFirst() * res.getSecond();
    }
}
