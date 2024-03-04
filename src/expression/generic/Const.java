package expression.generic;



import java.math.BigDecimal;
import java.util.List;

public class Const<T> implements ExpressionPart<T> {
    private final T val;

    public Const(T val) {
        this.val = val;
    }

    public T evaluate(T x) {
        return val;
    }
    public T evaluate(T x, T y, T z) {
        return val;
    }
    public String toString() {
        return val.toString();
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(val.toString());
    }
    public void toMiniString(StringBuilder res) {
        res.append(val.toString());
    }
    public String toMiniString() {
        return val.toString();
    }
    public int getPrior() {
        return 0;
    }

}
