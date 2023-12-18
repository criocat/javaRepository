package expression.parser;

import expression.Const;
import expression.ExpressionPart;
import expression.Multiply;
import expression.Variable;

import java.math.BigDecimal;

public class Low extends AbstractUnar implements ExpressionPart {
    public Low(ExpressionPart val) {
        super(val);
    }

    public String getOperation() {
        return "low";
    }
    public int evaluateByNum(int num) {
        if (num == 0) return 0;
        int cnt = 0;
        while(Math.abs(num % 2) != 1) {
            cnt++;
            num /= 2;
        }
        return (1 << cnt);
    }
}