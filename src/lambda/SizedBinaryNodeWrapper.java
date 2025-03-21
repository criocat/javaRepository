package info.kgeorgiy.ja.razinkov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees;

public class SizedBinaryNodeWrapper<T> extends BinaryNodeWrapper<T, Trees.SizedBinary<T>> {

    SizedBinaryNodeWrapper(Trees.SizedBinary<T> node) {
        super.node = node;
    }
    @Override
    BinaryNodeWrapper<T, Trees.SizedBinary<T>> clone(Trees.SizedBinary<T> node) {
        return new SizedBinaryNodeWrapper<>(node);
    }

    @Override
    Trees.SizedBinary<T> getLeft() {
        return ((Trees.SizedBinary.Branch<T>)node).left();
    }

    @Override
    Trees.SizedBinary<T> getRight() {
        return ((Trees.SizedBinary.Branch<T>)node).right();
    }

    @Override
    public boolean isLeaf() {
        return node instanceof Trees.Leaf<T>;
    }

    @Override
    public long getSize() {
        return node.size();
    }
}
