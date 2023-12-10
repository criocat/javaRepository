package expression;

import java.math.BigDecimal;

public class Divide extends AbstractOperation implements ExpressionPart {
    public Divide(ExpressionPart p1, ExpressionPart p2) {
        super(p1, p2, "/", 1);
    }
    public int evaluate(int x) {
        IntPair res = super.getResult(x);
        return res.getFirst() / res.getSecond();
    }
    public BigDecimal evaluate(BigDecimal x) {
        BigDecimalPair res = super.getDecResult(x);
        return res.getFirst().divide((res.getSecond()));
    }
    public int evaluate(int x, int y, int z) {
        IntPair res = super.getResult(x, y, z);
        return res.getFirst() / res.getSecond();
    }
}
