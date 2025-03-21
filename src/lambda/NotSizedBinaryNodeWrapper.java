package info.kgeorgiy.ja.razinkov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees;

public class NotSizedBinaryNodeWrapper<T> extends BinaryNodeWrapper<T, Trees.Binary<T>> {

    NotSizedBinaryNodeWrapper (Trees.Binary<T> node) {
        super.node = node;
    }

    @Override
    BinaryNodeWrapper<T, Trees.Binary<T>> clone(Trees.Binary<T> node) {
        return new NotSizedBinaryNodeWrapper<>(node);
    }

    @Override
    Trees.Binary<T> getLeft() {
        return ((Trees.Binary.Branch<T>)node).left();
    }

    @Override
    Trees.Binary<T> getRight() {
        return ((Trees.Binary.Branch<T>)node).right();
    }

    @Override
    public boolean isLeaf() {
        return node instanceof Trees.Leaf<T>;
    }

    @Override
    public long getSize() {
        return (isLeaf() ? 1 : Long.MAX_VALUE);
    }
}
