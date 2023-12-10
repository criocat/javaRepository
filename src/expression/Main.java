package expression;
import expression.parser.ExpressionParser;
import expression.parser.TripleParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ExpressionPart e = new Add(new Subtract(new Multiply(new Variable("x"),new Variable("x")), new Multiply(new Const(2), new Variable("x"))), new Const(1));
        int c;
        Scanner sc = new Scanner(System.in);
        //c = sc.nextInt();
        //System.out.println(e.evaluate(c));
        String str = "- 0";
        TripleParser pr = new ExpressionParser();
        ExpressionPart ex = (ExpressionPart) pr.parse(str);
        System.out.println(ex.toString());
    }
}
