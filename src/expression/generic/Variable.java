package expression.generic;



import java.math.BigDecimal;
import java.util.List;

public class Variable<T> implements ExpressionPart<T> {
    private final String str;

    public Variable(String str) {
        this.str = str;
    }

    public T evaluate(T x) {
        return x;
    }
    public T evaluate(T x, T y, T z) {
        if (str.equals("x")) {
            return x;
        } else if (str.equals("y")){
            return y;
        } else {
            return z;
        }
    }


    public String toString() {
        return str;
    }
    public void toString(StringBuilder strBuilder) {
        strBuilder.append(str);
    }
    public void toMiniString(StringBuilder res) {
        res.append(str);
    }
    public String toMiniString() {
        return str;
    }
    public int getPrior() {
        return 0;
    }
}
