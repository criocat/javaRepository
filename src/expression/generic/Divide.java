package expression.generic;




public class Divide<T> extends AbstractOperation<T> implements ExpressionPart<T> {
    private final AbstractNumberOperations<T> operations;
    public Divide(ExpressionPart<T> p1, ExpressionPart<T> p2, AbstractNumberOperations<T> operations) {
        super(p1, p2);
        this.operations = operations;
    }

    protected String getOperation() {
        return "/";
    }

    public int getPrior() {
        return 1;
    }

    protected T calc(T x, T y) throws RuntimeException {
        return operations.div(x, y);
    }
}
