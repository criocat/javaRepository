package expression.parser;
enum Operation {
    ADD(3), SUB(3), DIV(4), MUL(4), AND(2), XOR(1), OR(0);

    Operation(int prior) {
        this.prior = prior;
    }

    public int getPrior() {
        return prior;
    }

    final int prior;
}