package expression.exceptions;

import expression.*;
import expression.parser.Operation;

import java.math.BigDecimal;
import java.util.List;

public class ExpressionParser implements TripleParser, ListParser {
    private int pos;
    private String strExp;

    private List<String> variables;
    private final char END = (char) -2;

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
        return take() == '+' || take() == '-' || take() == '*' || take() == '/' || take() == '&' || take() == '|' || take() == '^' || expectedWord("<<") ||
                expectedWord(">>") || expectedWord(">>>") || expectedWord("min") || expectedWord("max");
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

    private ListExpression parsePartInUnaryOperation(int len) throws ParsingException {
        skip(len);
        skipSpaces();
        ListExpression e = null;
        if (expectedPart()) {
            e = parsePart();
        } else {
            throw new ParsingException("expected part, but found \'" + take() + "\'");
        }
        return e;
    }


    private CheckedNegate parseUnaryMinus() throws ParsingException {
        return new CheckedNegate((ExpressionPart) parsePartInUnaryOperation(1));
    }


    private Const parseConst() throws ParsingException {
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
        BigDecimal val = null;
        int overflow = 0;
        if (stringNum.length() >= 12) {
            overflow = (stringNum.charAt(0) == '-' ? 1 : 2);
        } else {
            val = BigDecimal.valueOf(Long.parseLong(stringNum.toString()));
            if (val.longValue() < Integer.MIN_VALUE || val.longValue() > Integer.MAX_VALUE) {
                overflow = (val.longValue() < 0 ? 1 : 2);
            }
        }
        if (overflow != 0) {
            throw new ParsingException("Constant overflow " + overflow);
        }
        skipSpaces();
        if (expectedConst()) {
            throw new ParsingException("Spaces in numbers");
        }
        return new Const(val);
    }

    private Operation parseOperation() {
        Operation res = switch (take()) {
            case '*' -> Operation.MUL;
            case '/' -> Operation.DIV;
            case '+' -> Operation.ADD;
            case '-' -> Operation.SUB;
            case '<' -> Operation.MOVEL;
            case '>' -> Operation.MOVER;
            default -> Operation.MIN;
        };
        if (expectedWord(">>>")) {
            res = Operation.SMOVER;
        } else if (expectedWord("max")) {
            res = Operation.MAX;
        }
        skip(res.getOperation().length());
        return res;
    }

    private ListExpression getResultOfOperation(ListExpression first, ListExpression second, Operation op) {
        return switch (op) {
            case ADD -> new CheckedAdd((ExpressionPart) first, (ExpressionPart) second);
            case SUB -> new CheckedSubtract((ExpressionPart) first, (ExpressionPart) second);
            case MUL -> new CheckedMultiply((ExpressionPart) first, (ExpressionPart) second);
            case DIV -> new CheckedDivide((ExpressionPart) first, (ExpressionPart) second);
            case MIN -> new Min((ExpressionPart) first, (ExpressionPart) second);
            case MAX -> new Max((ExpressionPart) first, (ExpressionPart) second);
            case MOVEL -> new MoveL((ExpressionPart) first, (ExpressionPart) second);
            case MOVER -> new MoveR((ExpressionPart) first, (ExpressionPart) second);
            case SMOVER -> new SignedMoveR((ExpressionPart) first, (ExpressionPart) second);
            default -> null;
        };
    }

    private Variable parseVariable() throws ParsingException {
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
        if (!variables.contains(str)) {
            if (take() == 'x' || take() == 'y' || take() == 'z') {
                throw new ParsingException("End symbol");
            } else {
                throw new ParsingException("Start symbol");
            }
        }
        int curpos = -1;
        for (int i = 0; i < variables.size(); ++i) {
            if (variables.get(i).equals(str)) {
                curpos = i;
                break;
            }
        }
        return new Variable(str, curpos);
    }


    private boolean expectedBracket() {
        return isOpenBracket(take());
    }

    private boolean expectedPart() {
        return expectedConst() || expectedVariable() || expectedUnaryOperation() || expectedBracket();
    }

    private ListExpression parseUnaryOperation() throws ParsingException {
        skipSpaces();
        if (expectedUnaryMinus()) {
            return parseUnaryMinus();
        } else {
            throw new ParsingException("expected part, but found '" + take() + "'");
        }
    }

    private ListExpression parsePart() throws ParsingException {
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

    private boolean isOpenBracket(char c) {
        return c == '(' || c == '[' || c == '{';
    }

    private boolean isCloseBracket(char c) {
        return c == ')' || c == ']' || c == '}';
    }

    private ListExpression parseBracket() throws ParsingException {
        char b = take();
        char revb = switch (b) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            default -> END;
        };
        skip();
        ListExpression res = parseExpression(revb, -1);
        skip();
        return res;
    }

    private ListExpression parseExpression(char endChar, int prior) throws ParsingException {
        skipSpaces();
        ListExpression secondPart = null, lastPart = null;
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
        if (!isCloseBracket(endChar) && isCloseBracket(take())) {
            throw new ParsingException("No opening parenthesis");
        } else if (isCloseBracket(endChar) && isCloseBracket(take())) {
            throw new ParsingException("incorrect closing parenthesis");
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

    public TripleExpression parse(String str) throws ParsingException {
        return (ExpressionPart) parse(str, List.of("x", "y", "z"));
    }

    public ListExpression parse(String str, List<String> variables) throws ParsingException {
        this.variables = variables;
        strExp = str;
        pos = 0;
        return parseExpression(END, -1);
    }
}
