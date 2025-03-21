package expression.exceptions;

import java.util.List;


@FunctionalInterface
public interface ListParser {
    ListExpression parse(String expression, final List<String> variables) throws Exception;
}
