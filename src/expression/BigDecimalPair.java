package expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalPair {
    private final BigDecimal first, second;
    public BigDecimalPair(BigDecimal first, BigDecimal second) {
        this.first = first;
        this.second = second;
    }
    public BigDecimal getFirst() {
        return first;
    }
    public BigDecimal getSecond() {
        return second;
    }
}
