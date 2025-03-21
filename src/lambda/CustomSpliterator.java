package info.kgeorgiy.ja.razinkov.lambda;

import java.util.*;
import java.util.function.Consumer;

public class CustomSpliterator <T> extends Spliterators.AbstractSpliterator<T> {
    private Deque<NodeWrapper<T>> parents;
    int rightInd;
    long size;

    public CustomSpliterator(NodeWrapper<T> tree, int characteristic) {
        super(tree.getSize(),
                characteristic |
                        Spliterator.ORDERED |
                        Spliterator.IMMUTABLE);
        rightInd = tree.childCount();
        size = tree.getSize();
        parents = new ArrayDeque<>(List.of(tree));
    }

    private void goLeft() {
        if (parents.isEmpty()) return;
        while (!parents.getLast().isLeaf()) {
            parents.add(parents.getLast().getNext(null));
        }
    }

    public NodeWrapper<T> next() {
        goLeft();
        if (parents.isEmpty()) return null;
        NodeWrapper<T> result = parents.getLast();
        size--;
        NodeWrapper<T> lastChild = parents.removeLast();
        while (!parents.isEmpty()) {
            NodeWrapper<T> next_child = parents.getLast().getNext(lastChild);
            if (next_child != null && (parents.size() != 1 || parents.getFirst().getChildPos(next_child) <= rightInd)) {
                parents.add(next_child);
                goLeft();
                break;
            }
            lastChild = parents.removeLast();
        }
        return result;
    }

    public NodeWrapper<T> current() {
        goLeft();
        return parents.isEmpty() ? null : parents.getLast();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        NodeWrapper<T> next_node = next();
        if (next_node == null) return false;
        action.accept(next_node.getValue());
        return true;
    }

    private void swap(CustomSpliterator<T> other) {
        Deque<NodeWrapper<T>> tempList = parents;
        int tempInd = rightInd;
        long tempSize = size;
        parents = other.parents;
        rightInd = other.rightInd;
        size = other.size;
        other.parents = tempList;
        other.rightInd = tempInd;
        other.size = tempSize;
    }

    @Override
    public CustomSpliterator<T> trySplit() {
        goLeft();
        while(parents.size() > 1) {
            Iterator<NodeWrapper<T>> it = parents.iterator();
            it.next();
            if (parents.getFirst().getChildPos(it.next()) >= rightInd) {
                parents.removeFirst();
                rightInd = parents.getFirst().childCount();
            } else {
                break;
            }
        }
        if (parents.size() <= 1) return null;
        NodeWrapper<T> new_root = parents.getFirst().getNthChild(rightInd - 1);
        CustomSpliterator<T> result = new CustomSpliterator<>(new_root, super.characteristics());
        rightInd--;
        size -= result.size;
        swap(result);
        return result;
    }

    @Override
    public long estimateSize() {
        if ((characteristics() & Spliterator.SIZED) == 0) {
            return Long.MAX_VALUE;
        }
        else if (parents.isEmpty() || parents.getFirst().isLeaf()) {
            return (parents.isEmpty() ? 0 : 1);
        } else {
            return size;
        }
    }

    @Override
    public long getExactSizeIfKnown() {
        return ((characteristics() & Spliterator.SIZED) == 0 ? -1 : estimateSize());
    }

    @Override
    public int characteristics() {
        return super.characteristics() | (parents.isEmpty() || parents.getFirst().isLeaf() ? Spliterator.SIZED | Spliterator.SUBSIZED : 0);
    }
}
