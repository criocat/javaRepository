package expression;

import java.math.BigDecimal;
import java.util.List;

public class Variable implements ExpressionPart {
    private final String str;
    int posInList;
    public Variable(String str) {
        this.str = str;
        posInList = -1;
    }

    public Variable(int posInList) {
        this.posInList = posInList;
        this.str = null;
    }

    public Variable(String str, int posInList) {
        this.posInList = posInList;
        this.str = str;
    }
    public int evaluate(int x) {
        return x;
    }
    public BigDecimal evaluate(BigDecimal x) {
        return x;
    }
    public int evaluate(int x, int y, int z) {
        if (str.equals("x")) {
            return x;
        } else if (str.equals("y")){
            return y;
        } else {
            return z;
        }
    }

    public int evaluate(List<Integer> variables) {
        return variables.get(posInList);
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
    public int hashCode() {
        return str.hashCode();
    }
    public boolean equals(Object object) {
        if (object != null && object.getClass() == Variable.class) {
            return toString().equals(object.toString());
        }
        return false;
    }
    public boolean equals(ExpressionPart l, ExpressionPart r) {
        return l == null && r == null;
    }
}
