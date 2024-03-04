package expression.generic;



public class Add<T> extends AbstractOperation<T> implements ExpressionPart<T> {
    private final AbstractNumberOperations<T> operations;
    public Add(ExpressionPart<T> p1, ExpressionPart<T> p2, AbstractNumberOperations<T> operations) {
        super(p1, p2);
        this.operations = operations;
    }

    public int getPrior() {
        return 3;
    }

    protected String getOperation() {
        return "+";
    }

    protected T calc(T x, T y) throws RuntimeException {
        return operations.add(x, y);
    }

}
