package expression.generic;




public class Subtract<T> extends AbstractOperation<T> implements ExpressionPart<T> {
    private final AbstractNumberOperations<T> operations;
    public Subtract(ExpressionPart<T> p1, ExpressionPart<T> p2, AbstractNumberOperations<T> operations) {
        super(p1, p2);
        this.operations = operations;
    }

    public int getPrior() {
        return 4;
    }

    protected String getOperation() {
        return "-";
    }

    protected T calc(T x, T y) throws RuntimeException {
        return operations.sub(x, y);
    }
}
