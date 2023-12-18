package expression;
import expression.parser.ExpressionParser;
import expression.parser.TripleParser;
import expression.ExpressionPart;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ExpressionPart e = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")), new Multiply(new Const(2), new Variable("x"))), new Const(1));
        Scanner sc = new Scanner(System.in);
        String str = "high 123";
        TripleParser pr = new ExpressionParser();
        TripleExpression exp = pr.parse(str);
        System.out.println(exp.toString());
    }
}
