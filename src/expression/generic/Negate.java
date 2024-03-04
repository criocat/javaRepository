package expression.generic;



public class Negate<T> extends AbstractUnary<T> implements ExpressionPart<T> {
    private final AbstractNumberOperations<T> operations;
    public Negate(ExpressionPart<T> val, AbstractNumberOperations<T> operations) {
        super(val);
        this.operations = operations;
    }

    protected String getOperation() {
        return "-";
    }

    protected T calc(T x) throws RuntimeException {
        return operations.negate(x);
    }
}
