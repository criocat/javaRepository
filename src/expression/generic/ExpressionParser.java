package expression.generic;


import java.math.BigDecimal;

public class ExpressionParser<T> {
    private int pos;
    private String strExp;

    private AbstractNumberOperations<T> operations;
    private final char END = (char) -1;

    private void skip() {
        if (pos != strExp.length()) {
            ++pos;
        }
    }

    private void skip(int cnt) {
        for (int i = 0; i < cnt; ++i) {
            if (pos != strExp.length()) {
                ++pos;
            }
        }
    }

    private char take() {
        return (pos >= strExp.length() ? END : strExp.charAt(pos));
    }

    private char takeNext() {
        return (pos + 1 >= strExp.length() ? END : strExp.charAt(pos + 1));
    }

    private boolean isSpace() {
        return Character.isWhitespace(take());
    }

    private void skipSpaces() {
        while (take() != END && isSpace()) {
            skip();
        }
    }

    private boolean expectedConst() {
        return Character.isDigit(take()) || (take() == '-' && Character.isDigit(takeNext()));
    }

    private boolean expectedVariable() {
        return Character.isJavaIdentifierStart(take());
    }

    private boolean expectedWord(String word) {
        int lastPos = pos;
        StringBuilder str = new StringBuilder();
        while (Character.isAlphabetic(take()) || Character.isDigit(take()) || take() == '>' || take() == '<') {
            str.append(take());
            skip();
        }
        pos = lastPos;
        return str.toString().equals(word);
    }

    private boolean expectedOperation() {
        return take() == '+' || take() == '-' || take() == '*' || take() == '/' || take() == '&' || take() == '|' || take() == '^';
    }

    private boolean expectedUnaryMinus() {
        int lastPos = pos;
        if (take() == '-' && !Character.isDigit(takeNext())) {
            skip();
            skipSpaces();
            boolean res = expectedPart();
            pos = lastPos;
            return res;
        }
        return false;
    }

    private boolean expectedUnaryOperation() {
        return expectedUnaryMinus();
    }

    private boolean isBare(char endChar) {
        int lastPos = pos;
        while (!Character.isWhitespace(take()) && take() != endChar && take() != END) {
            skip();
        }
        skipSpaces();
        boolean res = take() == endChar;
        pos = lastPos;
        return res;
    }

    private String parseWord(char endChar) {
        StringBuilder str = new StringBuilder();
        while (!Character.isWhitespace(take()) && take() != endChar && take() != END) {
            str.append(take());
            skip();
        }
        return str.toString();
    }

    private ExpressionPart<T> parsePartInUnaryOperation(int len) throws ParsingException {
        skip(len);
        skipSpaces();
        ExpressionPart<T> e = null;
        if (expectedPart()) {
            e = parsePart();
        } else {
            throw new ParsingException("expected part, but found '" + take() + "'");
        }
        return e;
    }


    private Negate<T> parseUnaryMinus() throws ParsingException {
        return new Negate<T>(parsePartInUnaryOperation(1), operations);
    }


    private Const<T> parseConst() throws ParsingException {
        boolean minus = (take() == '-');
        StringBuilder stringNum = new StringBuilder();
        if (minus) {
            stringNum.append('-');
            skip();
        }
        while (Character.isDigit(take())) {
            stringNum.append(take());
            skip();
        }
        int val;
        try {
            val = Integer.parseInt(stringNum.toString());
        } catch (Exception e) {
            throw new ParsingException("overflow");
        }
        return new Const<T>(operations.convertInt(val));
    }

    private Operation parseOperation() {
        Operation res = switch (take()) {
            case '*' -> Operation.MUL;
            case '/' -> Operation.DIV;
            case '+' -> Operation.ADD;
            case '-' -> Operation.SUB;
            default -> null;
        };
        skip(res.getOperation().length());
        return res;
    }

    private ExpressionPart<T> getResultOfOperation(ExpressionPart<T> first, ExpressionPart<T> second, Operation op) {
        return switch (op) {
            case Operation.ADD -> new Add<T>(first, second, operations);
            case Operation.SUB -> new Subtract<T>(first, second, operations);
            case Operation.MUL -> new Multiply<T>(first, second, operations);
            case Operation.DIV -> new Divide<T>(first, second, operations);
            default -> null;
        };
    }

    private Variable<T> parseVariable() throws ParsingException {
        StringBuilder var = new StringBuilder();
        skipSpaces();
        if (!Character.isJavaIdentifierStart(take())) {
            throw new ParsingException("Start symbol");
        }
        while (Character.isJavaIdentifierPart(take())) {
            var.append(take());
            skip();
        }
        String str = var.toString();
        return new Variable<T>(str);
    }


    private boolean expectedBracket() {
        return take() == '(';
    }

    private boolean expectedPart() {
        return expectedConst() || expectedVariable() || expectedUnaryOperation() || expectedBracket();
    }

    private ExpressionPart<T> parseUnaryOperation() throws ParsingException {
        skipSpaces();
        if (expectedUnaryMinus()) {
            return parseUnaryMinus();
        } else {
            throw new ParsingException("expected part, but found '" + take() + "'");
        }
    }

    private ExpressionPart<T> parsePart() throws ParsingException {
        if (expectedConst()) {
            return parseConst();
        } else if (expectedUnaryOperation()) {
            return parseUnaryOperation();
        } else if (expectedVariable()) {
            return parseVariable();
        } else {
            return parseBracket();
        }
    }



    private ExpressionPart<T> parseBracket() throws ParsingException {
        skip();
        ExpressionPart<T> res = parseExpression(')', -1);
        skip();
        return res;
    }

    private ExpressionPart<T> parseExpression(char endChar, int prior) throws ParsingException {
        skipSpaces();
        ExpressionPart<T> secondPart = null, lastPart = null;
        if (expectedPart()) {
            lastPart = parsePart();
        } else {
            throwExceptionBeforeParsing(endChar);
        }
        skipSpaces();
        while (expectedOperation()) {
            Operation op = parseOperation();
            int curPrior = op.getPrior();
            if (curPrior <= prior) {
                pos -= op.getOperation().length();
                return lastPart;
            } else {
                skipSpaces();
                if (expectedPart()) {
                    secondPart = parseExpression(endChar, curPrior);
                    lastPart = getResultOfOperation(lastPart, secondPart, op);
                    skipSpaces();
                } else {
                    throwExceptionInParsing(endChar);
                }
            }
        }
        skipSpaces();
        if (take() != endChar) {
            throwExceptionAfterParsing(endChar);
        }
        return lastPart;
    }

    private void throwExceptionInParsing(char endChar) throws ParsingException {
        if (expectedOperation()) {
            throw new ParsingException("No middle argument");
        } else if (take() == endChar) {
            throw new ParsingException("No last argument");
        }
        throw new ParsingException("expected part, but found '" + take() + "'");
    }

    private void throwExceptionAfterParsing(char endChar) throws ParsingException {
        if (take() == ')') {
            throw new ParsingException("No opening parenthesis");
        } else if (take() == END) {
            throw new ParsingException("No closing parenthesis");
        }
        skip();
        skipSpaces();
        if (take() == endChar) {
            throw new ParsingException("End symbol");
        } else {
            throw new ParsingException("Middle symbol");
        }
    }

    private void throwExceptionBeforeParsing(char endChar) throws ParsingException {
        if (endChar == ')' && take() == ')') {
            throw new ParsingException("Empty Brackets");
        }
        if (isBare(endChar)) {
            String str = parseWord(endChar);
            throw new ParsingException("Bare " + str);
        }
        if (expectedOperation()) {
            throw new ParsingException("No first argument");
        }
        throw new ParsingException("Start symbol");
    }


    public ExpressionPart<T> parse(String str, AbstractNumberOperations<T> operations) throws ParsingException {
        this.operations = operations;
        strExp = str;
        pos = 0;
        return parseExpression(END, -1);
    }
}
