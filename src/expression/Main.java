package expression;

import java.util.Scanner;
import expression.exceptions.TripleParser;
import expression.exceptions.ExpressionParser;

public class Main {

    public static void main(String[] args) {
        ExpressionPart e = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")), new Multiply(new Const(2), new Variable("x"))), new Const(1));
        Scanner sc = new Scanner(System.in);
        String str = "x min (y max y)";
        TripleParser pr = new ExpressionParser();
        try {
            TripleExpression exp = pr.parse(str);
            System.out.println(((ExpressionPart)exp).toMiniString());
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }
}
