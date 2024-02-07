package expression;



import java.util.Scanner;

import expression.exceptions.ExpressionParser;
import expression.exceptions.TripleParser;

public class Main {

    public static void main(String[] args) {
        ExpressionPart e = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")), new Multiply(new Const(2), new Variable("x"))), new Const(1));
        TripleParser p = new ExpressionParser();
        try {
        TripleExpression exp = p.parse("+228");
        System.out.println(((ExpressionPart)exp).toString());
        }
        catch (Exception ec) {
            System.out.println(ec.getMessage());
        }
    }
}
