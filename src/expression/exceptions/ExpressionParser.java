package expression.exceptions;

import expression.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import expression.parser.Operation;

public class ExpressionParser implements TripleParser {
    private int pos;
    private String strExp;
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
        return take() == 'x' || take() == 'y' || take() == 'z';
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

    private boolean expectUnarMinus() {
        int lastPos = pos;
        if(take() == '-' && !Character.isDigit(takeNext())) {
            skip();
            skipSpaces();
            boolean res = expectedPart();
            pos = lastPos;
            return res;
        }
        return false;
    }

    private boolean expectedUnarOperation() {
        return expectUnarMinus();
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

    private TripleExpression parsePartInUnarOperation(int len) throws Exception {
        skip(len);
        skipSpaces();
        TripleExpression e = null;
        if (expectedPart()) {
            e = parsePart();
        } else {
            throw new Exception("expected part, but found \'" + take() + "\'");
        }
        return e;
    }


    private CheckedNegate parseUnarMinus() throws Exception {
        return new CheckedNegate((ExpressionPart) parsePartInUnarOperation(1));
    }


    private Const parseConst() throws Exception {
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
            throw new Exception("Constant overflow " + overflow);
        }
        skipSpaces();
        if (expectedConst()) {
            throw new Exception("Spaces in numbers");
        }
        return new Const(val);
    }

    private Operation parseOperation() {
        Operation res = switch (take()) {
            case '*' -> Operation.MUL;
            case '/' -> Operation.DIV;
            case '+' -> Operation.ADD;
            case '-' -> Operation.SUB;
            default -> null;
        };
        if (res == null) {
            if (expectedWord(">>>")) {
                res = Operation.SMOVER;
                skip(2);
            }
            else if (expectedWord(">>")) {
                res = Operation.MOVER;
                skip(1);
            } else if (expectedWord("min")) {
                res = Operation.MIN;
                skip(2);
            } else if (expectedWord("<<")) {
                res = Operation.MOVEL;
                skip(1);
            }
            else {
                res = Operation.MAX;
                skip(2);
            }
        }
        skip();
        return res;
    }

    private TripleExpression getResultOfOperation(TripleExpression first, TripleExpression second, Operation op) {
        return switch (op) {
            case Operation.ADD -> new CheckedAdd((ExpressionPart) first, (ExpressionPart) second);
            case Operation.SUB -> new CheckedSubtract((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MUL -> new CheckedMultiply((ExpressionPart) first, (ExpressionPart) second);
            case Operation.DIV -> new CheckedDivide((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MIN -> new Min((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MAX -> new Max((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MOVEL -> new MoveL((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MOVER -> new MoveR((ExpressionPart) first, (ExpressionPart) second);
            case Operation.SMOVER -> new SignedMoveR((ExpressionPart) first, (ExpressionPart) second);
            default -> null;
        };
    }

    private Variable parseVariable() throws Exception {
        StringBuilder var = new StringBuilder();
        while (Character.isDigit(take()) || Character.isAlphabetic(take())) {
            var.append(take());
            skip();
        }
        String str = var.toString();
        if (!str.equals("x") && !str.equals("y") && !str.equals("z")) {
            if (take() == 'x' || take() == 'y' || take() == 'z') {
                throw new Exception("Start symbol");
            } else {
                throw new Exception("End symbol");
            }
        }
        return new Variable(str);
    }


    private boolean expectedBracket() {
        return take() == '(';
    }

    private boolean expectedPart() {
        return expectedConst() || expectedVariable() || expectedUnarOperation() || expectedBracket();
    }

    private TripleExpression parseUnarOperation() throws Exception {
        skipSpaces();
        if (expectUnarMinus()) {
            return parseUnarMinus();
        } else {
            throw new Exception("expected part, but found \'" + take() + "\'");
        }
    }

    private TripleExpression parsePart() throws Exception {
        if (expectedConst()) {
            return parseConst();
        } else if (expectedUnarOperation()) {
            return parseUnarOperation();
        } else if (expectedVariable()) {
            return parseVariable();
        } else {
            return parseBracket();
        }
    }

    private TripleExpression parseBracket() throws Exception {
        skip();
        TripleExpression res = parseExpression(')', -1);
        skip();
        return res;
    }

    private TripleExpression parseExpression(char endChar, int prior) throws Exception {
        skipSpaces();
        TripleExpression secondPart = null, lastPart = null;
        if (expectedPart()) {
            lastPart = parsePart();
        } else {
            if (endChar == ')' && take() == ')') {
                throw new Exception("Empty Brackets");
            }
            if (isBare(endChar)) {
                String str = parseWord(endChar);
                throw new Exception("Bare " + str);
            }
            if (expectedOperation()) {
                throw new Exception("No first argument");
            }
            throw new Exception("Start symbol");
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
                    if (expectedOperation()) {
                        throw new Exception("No middle argument");
                    } else if (take() == endChar) {
                        throw new Exception("No last argument");
                    }
                    throw new Exception("expected part, but found \'" + take() + "\'");
                }
            }
        }
        skipSpaces();
        if (take() != endChar) {
            if (take() == ')') {
                throw new Exception("No opening parenthesis");
            } else if (take() == END) {
                throw new Exception("No closing parenthesis");
            }
            skip();
            skipSpaces();
            if (take() == endChar) {
                throw new Exception("End symbol");
            }
            else {
                throw new Exception("Middle symbol");
            }
        }
        return lastPart;
    }

    public TripleExpression parse(String str) throws Exception {
        strExp = str;
        pos = 0;
        return parseExpression(END, -1);
    }
}
