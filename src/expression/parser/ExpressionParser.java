package expression.parser;
import expression.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExpressionParser implements expression.parser.TripleParser {
    int pos;
    String strExp;
    char END = (char)-2;


    private enum Operation {
        ADD, SUB, DEL, MUL, AND, OR, XOR;
    }

    private char goNext() {
        return (pos == strExp.length() ? END : strExp.charAt(pos++));
    }
    private char take() {
        return strExp.charAt(pos);
    }

    private char takeNext() {
        return (pos + 1 >= strExp.length() ? END : strExp.charAt(pos + 1));
    }

    private boolean isSpace() {
        return Character.isWhitespace(take());
    }

    private void skipSpaces() {
        while(take() != END && isSpace()) {
            goNext();
        }
    }

    private Const parseConst() {
        boolean minus = (take() == '-');
        if (minus) {
            goNext();
        }
        StringBuilder stringNum = new StringBuilder();
        while(Character.isDigit(take())) {
            stringNum.append(take());
            goNext();
        }
        BigDecimal val = BigDecimal.valueOf(Long.parseLong(stringNum.toString()));
        if (minus) {
            val = val.multiply(BigDecimal.valueOf(-1));
        }
        return new Const(val);
    }

    private Operation parseOperation() {
        return switch (goNext()) {
            case '*' -> Operation.MUL;
            case '/' -> Operation.DEL;
            case '+' -> Operation.ADD;
            case '-' -> Operation.SUB;
            case '&' -> Operation.AND;
            case '|' -> Operation.OR;
            case '^' -> Operation.XOR;
            default -> null;
        };
    }

    private Variable parseVariable() {
        return new Variable("" + goNext());
    }

    private TripleExpression parseBracket() {
        goNext();
        ArrayList<Operation> operations = new ArrayList<>();
        ArrayList<TripleExpression> expressions = new ArrayList<>();
        int doMinus = 0;
        boolean isOp = true;
        while(take() != ')') {
            skipSpaces();
            char curChar = take();
            TripleExpression e = null;
            if (isOp && (curChar == '-' && Character.isDigit(takeNext())) || Character.isDigit(take())) {
                e = parseConst();
            }
            else if (isOp && curChar == '-') {
                doMinus++;
                goNext();
            }
            else if (curChar == '*' || curChar == '/' || curChar == '+' || curChar == '-' || curChar == '&' || curChar == '|' || curChar == '^') {
                operations.add(parseOperation());
                isOp = true;
            }
            else if (curChar == 'x' || curChar == 'y' || curChar == 'z') {
                e = parseVariable();
            } else {
                e = parseBracket();
            }
            skipSpaces();
            while (doMinus > 0 && e != null) {
                doMinus--;
                e = new Negative((ExpressionPart) e);
            }
            if (e != null) {
                isOp = false;
                expressions.add(e);
            }
        }
        goNext();
        ArrayList<Operation> newOperations = new ArrayList<>();
        ArrayList<TripleExpression> newExpressions = new ArrayList<>();
        newExpressions.add(expressions.get(0));
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.MUL || operations.get(i) == Operation.DEL) {
                TripleExpression curExpr = null;
                if (operations.get(i) == Operation.DEL) {
                    curExpr = new Divide((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                } else {
                    curExpr = new Multiply((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                }
                newExpressions.remove(newExpressions.size() - 1);
                newExpressions.add(curExpr);
            }
            else {
                newOperations.add(operations.get(i));
                newExpressions.add(expressions.get(i + 1));
            }
        }
        expressions = newExpressions;
        operations = newOperations;

        newExpressions.add(expressions.get(0));
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.ADD || operations.get(i) == Operation.SUB) {
                TripleExpression curExpr = null;
                if (operations.get(i) == Operation.ADD) {
                    curExpr = new Add((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                } else {
                    curExpr = new Subtract((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                }
                newExpressions.remove(newExpressions.size() - 1);
                newExpressions.add(curExpr);
            }
            else {
                newOperations.add(operations.get(i));
                newExpressions.add(expressions.get(i + 1));
            }
        }
        expressions = newExpressions;
        operations = newOperations;

        newExpressions.add(expressions.get(0));
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.AND) {
                TripleExpression curExpr = new Divide((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                newExpressions.remove(newExpressions.size() - 1);
                newExpressions.add(curExpr);
            }
            else {
                newOperations.add(operations.get(i));
                newExpressions.add(expressions.get(i + 1));
            }
        }
        expressions = newExpressions;
        operations = newOperations;

        newExpressions.add(expressions.get(0));
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.XOR) {
                TripleExpression curExpr = null;
                if (operations.get(i) == Operation.DEL) {
                    curExpr = new Divide((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                } else {
                    curExpr = new Multiply((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                }
                newExpressions.remove(newExpressions.size() - 1);
                newExpressions.add(curExpr);
            }
            else {
                newOperations.add(operations.get(i));
                newExpressions.add(expressions.get(i + 1));
            }
        }
        expressions = newExpressions;
        operations = newOperations;

        newExpressions.add(expressions.get(0));
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.OR) {
                TripleExpression curExpr = null;
                if (operations.get(i) == Operation.DEL) {
                    curExpr = new Divide((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                } else {
                    curExpr = new Multiply((ExpressionPart) newExpressions.get(newExpressions.size() - 1),(ExpressionPart) expressions.get(i + 1));
                }
                newExpressions.remove(newExpressions.size() - 1);
                newExpressions.add(curExpr);
            }
            else {
                newOperations.add(operations.get(i));
                newExpressions.add(expressions.get(i + 1));
            }
        }
        expressions = newExpressions;
        operations = newOperations;

        TripleExpression res = expressions.get(0);
        for (int i = 0; i < operations.size(); ++i) {
            if (operations.get(i) == Operation.ADD) {
                res = new Add((ExpressionPart) res, (ExpressionPart)expressions.get(i + 1));
            }
            else {
                res = new Subtract((ExpressionPart) res, (ExpressionPart)expressions.get(i + 1));
            }
        }
        return res;
    }

    public TripleExpression parse (String str) {
        try {
        PrintWriter pw = new PrintWriter("outputasfasfadfasfdasdfa.txt", StandardCharsets.UTF_8);
        pw.write(str);
        pw.write('\n');
        pw.close();
        } catch (Exception e) {

        }
        strExp = "(" + str + ") + 0";
        pos = 0;
        return parseBracket();
    }
}
