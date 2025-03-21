package expression.exceptions;


@FunctionalInterface
public interface TripleParser {
    TripleExpression parse(String expression) throws Exception;
}
