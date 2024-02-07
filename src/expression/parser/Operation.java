package expression.parser;
public enum Operation {
    ADD(3, "+"), SUB(3, "-"), DIV(4, "/"), MUL(4, "*"), MOVEL(2, "<<"),
    MOVER(2, ">>"), SMOVER(2, ">>>"), MIN(2, "min"), MAX(2, "max");

    Operation(int prior, String opStr) {
        this.prior = prior;
        this.opStr = opStr;
    }

    public int getPrior() {
        return prior;
    }
    public String getOperation() {
        return opStr;
    }
    private final int prior;
    private final String opStr;
}