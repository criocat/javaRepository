package info.kgeorgiy.ja.razinkov.lambda;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class NestedCustomSpliterator<T> implements Spliterator<T> {
    CustomSpliterator<List<T>> treeSpliterator;
    Spliterator<T> listSpliterator;
    int characteristic;

    NestedCustomSpliterator(CustomSpliterator<List<T>> sit) {
        treeSpliterator = sit;
        listSpliterator = null;
        characteristic = Spliterator.ORDERED | (isSized() ? (Spliterator.SUBSIZED | Spliterator.SIZED) : 0);
    }

    private long listEstimateSize() {
        initSpliterator();
        return listSpliterator.estimateSize();
    }

    private void initSpliterator() {
        if (listSpliterator == null) {
            listSpliterator = treeSpliterator.current().getValue().spliterator();
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        initSpliterator();
        if (!listSpliterator.tryAdvance(action)) {
            return false;
        }
        if (listSpliterator.estimateSize() == 0) {
            treeSpliterator.next();
            if (treeSpliterator.current() != null) {
                listSpliterator = treeSpliterator.current().getValue().spliterator();
            }
        }
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        initSpliterator();
        CustomSpliterator<List<T>> pref = treeSpliterator.trySplit();
        if (pref != null) {
            NestedCustomSpliterator<T> res = new NestedCustomSpliterator<>(pref);
            res.listSpliterator = listSpliterator;
            listSpliterator = treeSpliterator.current().getValue().spliterator();
            return res;
        } else {
            Spliterator<T> listSplit = listSpliterator.trySplit();
            if (listSplit != null) {
                NestedCustomSpliterator<T> res = new NestedCustomSpliterator<>(treeSpliterator);
                res.listSpliterator = listSplit;
                return res;
            } else {
                return null;
            }
        }
    }

    @Override
    public long estimateSize() {
        if ((characteristic & Spliterator.SIZED) != 0) {
            return listEstimateSize();
        } else {
            return Long.MAX_VALUE;
        }
    }

    private boolean isSized() {
        long val = treeSpliterator.getExactSizeIfKnown();
        return val == 0 | val == 1;
    }

    @Override
    public long getExactSizeIfKnown() {
        if ((characteristic & Spliterator.SIZED) != 0) {
            return listEstimateSize();
        } else {
            return -1;
        }
    }

    @Override
    public int characteristics() {
        return characteristic;
    }
}
