package info.kgeorgiy.ja.razinkov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees;

import java.util.List;

public class NaryNodeWrapper <T> implements NodeWrapper<T> {
    Trees.Nary<T> node;
    int pos;

    public NaryNodeWrapper(Trees.Nary<T> node) {
        this.node = node;
        this.pos = 0;
    }

    @Override
    public NodeWrapper<T> getNext(NodeWrapper<T> currentChild) {
        if (node instanceof Trees.Nary.Node<T>(List<Trees.Nary<T>> children) && children.size() != pos) {
            pos++;
            return new NaryNodeWrapper<>(children.get(pos - 1));
        }
        return null;
    }

    @Override
    public int getChildPos(NodeWrapper<T> currentChild) {
        return pos;
    }

    @Override
    public boolean isLeaf() {
        return node instanceof Trees.Leaf<T>;
    }

    @Override
    public int childCount() {
        return (isLeaf() ? 0 : ((Trees.Nary.Node<T>)node).children().size());
    }

    @Override
    public NodeWrapper<T> getNthChild(int pos) {
        if (node instanceof Trees.Nary.Node<T>(List<Trees.Nary<T>> childrens)) {
            return new NaryNodeWrapper<>(childrens.get(pos));
        }
        throw new RuntimeException("method cannot be called on leaf");
    }

    @Override
    public long getSize() {
        return (isLeaf() ? 1 : Long.MAX_VALUE);
    }

    @Override
    public T getValue() {
        return ((Trees.Leaf<T>)node).value();
    }
}
