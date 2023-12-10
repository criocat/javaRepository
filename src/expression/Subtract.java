package expression;

import java.math.BigDecimal;

public class Subtract extends AbstractOperation implements ExpressionPart {
    public Subtract(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2, "-", 4);
    }
    public int evaluate(int x) {
        IntPair res = super.getResult(x);
        return res.getFirst() - res.getSecond();
    }
    public BigDecimal evaluate(BigDecimal x) {
        BigDecimalPair res = super.getDecResult(x);
        return res.getFirst().subtract((res.getSecond()));
    }
    public int evaluate(int x, int y, int z) {
        IntPair res = super.getResult(x, y, z);
        return res.getFirst() - res.getSecond();
    }
}
