package expression.exceptions;

import base.Selector;
import expression.ListExpression;
import expression.TripleExpression;

import static expression.parser.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ExceptionsTest {
    private static final ExpressionParser PARSER = new ExpressionParser();
    private static final Operation TRIPLE = kind(TripleExpression.KIND, (expr, variables) -> PARSER.parse(expr));
    private static final Operation LIST = kind(ListExpression.KIND, PARSER::parse);

    private static final Operation PARENS = tester -> tester.parens("{", "}", "[", "]");


    public static final Selector SELECTOR = Selector.composite(ExceptionsTest.class, ExceptionsTester::new, "easy", "hard")
            .variant("Base", TRIPLE, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("MinMax", MIN, MAX)
            .variant("Shifts", SHIFT_L, SHIFT_R, SHIFT_A)
            .variant("Parens", PARENS)
            .variant("List", LIST)
            .selector();

    private ExceptionsTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}