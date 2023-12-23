package expression.parser;

import expression.ExpressionPart;

public class High extends AbstractUnar implements ExpressionPart {
    public High(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "high";
    }
    public int evaluateByNum(int num) {
        if (num == 0) return 0;
        int cnt = 0;
        while(num != 1) {
            cnt++;
            num = (num >>> 1);
        }
        return (1 << cnt);
    }
}
