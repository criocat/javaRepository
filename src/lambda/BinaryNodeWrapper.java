package info.kgeorgiy.ja.razinkov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees;

public abstract class BinaryNodeWrapper <T, Node> implements NodeWrapper<T> {
    Node node;

    abstract BinaryNodeWrapper<T, Node> clone(Node node);
    abstract Node getLeft();
    abstract Node getRight();

    @Override
    @SuppressWarnings("unchecked")
    public NodeWrapper<T> getNext(NodeWrapper<T> currentChild) {
        if (!isLeaf()) {
            if (currentChild == null) {
                return clone(getLeft());
            } else if (((BinaryNodeWrapper<T, Node>)currentChild).node == getLeft()) {
                return clone(getRight());
            } else {
                return null;
            }
        }
        throw new RuntimeException("method cannot be called on leaf");
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getChildPos(NodeWrapper<T> currentChild) {
        if (currentChild == null) return 0;
        if (!isLeaf()) {
            if (((BinaryNodeWrapper<T, Node>)currentChild).node == getLeft()) return 1;
            else return 2;
        }
        throw new RuntimeException("method cannot be called on leaf");
    }

    @Override
    public int childCount() {
        if (isLeaf()) return 0;
        else return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        return ((Trees.Leaf<T>)node).value();
    }

    @Override
    public NodeWrapper<T> getNthChild(int pos) {
        if (!isLeaf()) {
            return switch (pos) {
                case 0 -> clone(getLeft());
                case 1 -> clone(getRight());
                default -> throw new RuntimeException("invalid index");
            };
        }
        throw new RuntimeException("method cannot be called on leaf");
    }
}
