package expression.parser;

import expression.ExpressionPart;

public class L1 extends AbstractUnar implements ExpressionPart {
    public L1(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "l1";
    }

    public int evaluateByNum(int num) {
        int[] a = new int[32];
        for (int i = a.length - 1; i >= 0; --i) {
            a[i] = Math.abs(num % 2);
            num = (num >>> 1);
        }
        int res = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] == 1) {
                res = i + 1;
            } else {
                break;
            }
        }
        return res;
    }

}
