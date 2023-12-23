package expression.parser;

import expression.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

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
        int lastPos = pos;
        StringBuilder strNum = new StringBuilder();
        if (take() == '-') {
            strNum.append(take());
            skip();
        }
        if (!Character.isDigit(take())) {
            pos = lastPos;
            return false;
        }
        while (take() != END && Character.isDigit(take())) {
            strNum.append(take());
            skip();
        }
        pos = lastPos;
        if (strNum.length() > 11) {
            return false;
        }
        long num = Long.parseLong(strNum.toString());
        return Math.abs(num) < (1L << 32);
    }

    private boolean expectedVariable() {
        return Character.isAlphabetic(take());
    }

    private boolean expectedOperation() {
        return take() == '+' || take() == '-' || take() == '*' || take() == '/' || take() == '&' || take() == '|' || take() == '^';
    }

    private boolean expectedWord(String word) {
        int lastPos = pos;
        StringBuilder str = new StringBuilder();
        while (Character.isAlphabetic(take()) || Character.isDigit(take())) {
            str.append(take());
            skip();
        }
        pos = lastPos;
        return str.toString().equals(word);
    }

    private boolean expectLow() {
        return expectedWord("low");
    }

    private boolean expectHigh() {
        return expectedWord("high");
    }

    private boolean expectL1() {
        return expectedWord("l1");
    }

    private boolean expectT1() {
        return expectedWord("t1");
    }

    private boolean expectUnarMinus() {
        return take() == '-' && !Character.isDigit(takeNext());
    }

    private boolean expectedUnarOperation() {
        return expectUnarMinus() || expectLow() || expectHigh() || expectL1() || expectT1();
    }

    private TripleExpression parsePartInUnarOperation(int len) {
        skip(len);
        skipSpaces();
        TripleExpression e = null;
        if (expectedPart()) {
            e = parsePart();
        } else {
            throw new RuntimeException("expected part, but found \'" + take() + "\'");
        }
        return e;
    }

    private Low parseLow() {
        return new Low((ExpressionPart) parsePartInUnarOperation(3));
    }

    private High parseHigh() {
        return new High((ExpressionPart) parsePartInUnarOperation(4));
    }

    private L1 parseL1() {
        return new L1((ExpressionPart) parsePartInUnarOperation(2));
    }

    private T1 parseT1() {
        return new T1((ExpressionPart) parsePartInUnarOperation(2));
    }

    private Negative parseUnarMinus() {
        return new Negative((ExpressionPart) parsePartInUnarOperation(1));
    }


    private Const parseConst() {
        boolean minus = (take() == '-');
        if (minus) {
            skip();
        }
        StringBuilder stringNum = new StringBuilder();
        while (Character.isDigit(take())) {
            stringNum.append(take());
            skip();
        }
        BigDecimal val = BigDecimal.valueOf(Long.parseLong(stringNum.toString()));
        if (minus) {
            val = val.multiply(BigDecimal.valueOf(-1));
        }
        return new Const(val);
    }

    private Operation parseOperation() {
        Operation res = switch (take()) {
            case '*' -> Operation.MUL;
            case '/' -> Operation.DIV;
            case '+' -> Operation.ADD;
            case '-' -> Operation.SUB;
            case '&' -> Operation.AND;
            case '|' -> Operation.OR;
            case '^' -> Operation.XOR;
            default -> null;
        };
        skip();
        return res;
    }

    private TripleExpression getResultOfOperation(TripleExpression first, TripleExpression second, Operation op) {
        return switch (op) {
            case Operation.ADD -> new Add((ExpressionPart) first, (ExpressionPart) second);
            case Operation.SUB -> new Subtract((ExpressionPart) first, (ExpressionPart) second);
            case Operation.MUL -> new Multiply((ExpressionPart) first, (ExpressionPart) second);
            case Operation.DIV -> new Divide((ExpressionPart) first, (ExpressionPart) second);
            case Operation.AND -> new And((ExpressionPart) first, (ExpressionPart) second);
            case Operation.XOR -> new Xor((ExpressionPart) first, (ExpressionPart) second);
            case Operation.OR -> new Or((ExpressionPart) first, (ExpressionPart) second);
            default -> null;
        };
    }

    private Variable parseVariable() {
        StringBuilder var = new StringBuilder();
        while (Character.isDigit(take()) || Character.isAlphabetic(take())) {
            var.append(take());
            skip();
        }
        return new Variable(var.toString());
    }


    private boolean expectedBracket() {
        return take() == '(';
    }

    private boolean expectedPart() {
        return expectedConst() || expectedVariable() || expectedUnarOperation() || expectedBracket();
    }

    private TripleExpression parseUnarOperation() {
        skipSpaces();
        if (expectUnarMinus()) {
            return parseUnarMinus();
        } else if (expectLow()) {
            return parseLow();
        } else if (expectHigh()) {
            return parseHigh();
        } else if (expectL1()) {
            return parseL1();
        } else if (expectT1()) {
            return parseT1();
        } else {
            throw new RuntimeException("expected part, but found \'" + take() + "\'");
        }
    }

    private TripleExpression parsePart() {
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

    private TripleExpression parseBracket() {
        skip();
        TripleExpression res = parseExpression(')', -1);
        skip();
        return res;
    }

    private TripleExpression parseExpression(char endChar, int prior) {
        skipSpaces();
        TripleExpression secondPart = null, lastPart = null;
        if (expectedPart()) {
            lastPart = parsePart();
        } else {
            throw new RuntimeException("expected part, but found nothing");
        }
        skipSpaces();
        while (expectedOperation()) {
            Operation op = parseOperation();
            int curPrior = op.getPrior();
            if (curPrior <= prior) {
                pos--;
                return lastPart;
            } else {
                skipSpaces();
                if (expectedPart()) {
                    secondPart = parseExpression(endChar, curPrior);
                    lastPart = getResultOfOperation(lastPart, secondPart, op);
                    skipSpaces();
                } else {
                    throw new RuntimeException("expected part, but found \'" + take() + "\'");
                }
            }
        }
        skipSpaces();
        if (take() != endChar) {
            throw new RuntimeException("expected " + endChar + ", but found \'" + take() + "\'");
        }
        return lastPart;
    }

    public TripleExpression parse(String str) {
        strExp = str;
        pos = 0;
        return parseExpression(END, -1);
    }
}
