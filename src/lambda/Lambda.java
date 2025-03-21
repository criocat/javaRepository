package info.kgeorgiy.ja.razinkov.lambda;
import info.kgeorgiy.java.advanced.lambda.AdvancedLambda;
import info.kgeorgiy.java.advanced.lambda.Trees;
import net.java.quickcheck.collection.Pair;
import net.java.quickcheck.collection.Triple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Lambda implements AdvancedLambda {

    @Override
    public <T> CustomSpliterator<T> binaryTreeSpliterator(Trees.Binary<T> tree) {
        return new CustomSpliterator<T>(new NotSizedBinaryNodeWrapper<T>(tree), 0);
    }

    @Override
    public <T> CustomSpliterator<T> sizedBinaryTreeSpliterator(Trees.SizedBinary<T> tree) {
        return new CustomSpliterator<T>(new SizedBinaryNodeWrapper<T>(tree),
                Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    @Override
    public <T> CustomSpliterator<T> naryTreeSpliterator(Trees.Nary<T> tree) {
        return new CustomSpliterator<T>(new NaryNodeWrapper<T>(tree), 0);
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> first() {
        return Collectors.reducing(Optional.empty(), Optional::of,
                (res, other) -> (res.isPresent() ? res : other));
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> last() {
        return Collectors.reducing(Optional.empty(), Optional::of, (_, res) -> res);
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> middle() {
        return Collectors.collectingAndThen(Collectors.toList(),
                (List<T> list) -> list.isEmpty() ? Optional.empty() : Optional.of(list.get(list.size() / 2)));
    }

    @Override
    public Collector<CharSequence, ?, String> commonPrefix() {
        return Collectors.collectingAndThen(Collectors.reducing((CharSequence s1, CharSequence s2) -> {
            int commonLen = 0;
            for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                if (s1.charAt(i) != s2.charAt(i)) break;
                commonLen++;
            }
            return s1.subSequence(0, commonLen);
        }), (opt) -> opt.map(CharSequence::toString).orElse(""));
    }

    @Override
    public Collector<CharSequence, ?, String> commonSuffix() {
        return Collectors.collectingAndThen(Collectors.reducing((CharSequence s1, CharSequence s2) -> {
            int commonLen = 0;
            for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                if (s1.charAt(s1.length() - i - 1) != s2.charAt(s2.length() - i - 1)) break;
                commonLen++;
            }
            return s1.subSequence(s1.length() - commonLen, s1.length());
        }), (opt) -> opt.map(CharSequence::toString).orElse(""));
    }

    @Override
    public <T> Spliterator<T> nestedBinaryTreeSpliterator(Trees.Binary<List<T>> tree) {
        return new NestedCustomSpliterator<T>(binaryTreeSpliterator(tree));
    }

    @Override
    public <T> Spliterator<T> nestedSizedBinaryTreeSpliterator(Trees.SizedBinary<List<T>> tree) {
        return new NestedCustomSpliterator<T>(sizedBinaryTreeSpliterator(tree));
    }

    @Override
    public <T> Spliterator<T> nestedNaryTreeSpliterator(Trees.Nary<List<T>> tree) {
        return new NestedCustomSpliterator<T>(naryTreeSpliterator(tree));
    }

    @Override
    public <T> Collector<T, ?, List<T>> head(int k) {
        return Collectors.reducing(List.of(), (T v) -> new ArrayList<T>(List.of(v)), (list,el) -> {
            List<T> res = (list.isEmpty() ? new ArrayList<>() : list);
            if (res.size() < k) {
                res.add(el.getFirst());
            }
            return res;
        });
    }

    @Override
    public <T> Collector<T, ?, List<T>> tail(int k) {
        return Collectors.collectingAndThen(
                Collectors.reducing(new ArrayDeque<T>(), (T v) -> (new ArrayDeque<T>(List.of(v))),
                        (list, el) -> {
                            ArrayDeque<T> res = (list.isEmpty() ? new ArrayDeque<>() : list);
                            res.add(el.getFirst());
                            if (res.size() >= k + 1) res.removeFirst();
                            return res;
                        }),
                (v) -> v.stream().toList());
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> kth(int k) {
        return Collectors.collectingAndThen(Collectors.reducing(new Pair<>(null, -1), (T v) -> new Pair<>(v, -1), (Pair<T, Integer> p1,Pair<T, Integer> p2) -> {
            if (p1.getSecond() == k) return p1;
            else return new Pair<>(p2.getFirst(), p1.getSecond() + 1);
        }), (p) -> p.getSecond() == k && k != -1 ? Optional.of(p.getFirst()) : Optional.empty());
    }

    @Override
    public <T> Collector<T, ?, List<T>> distinctBy(Function<? super T, ?> mapper) {
        return Collectors.collectingAndThen(Collectors.toMap(mapper, v -> v, (v1, _) -> v1, LinkedHashMap::new),
                m -> m.values().stream().toList());
    }

    @Override
    public <T> Collector<T, ?, OptionalLong> minIndex(Comparator<? super T> comparator) {
        return Collectors.collectingAndThen(Collectors.reducing(new Triple<T, Long, Long>(null, -1L, -1L),
                (T v) -> new Triple<>(v, -1L, -1L), (t1, t2) -> {
            if (t1.getFirst() == null || comparator.compare(t1.getFirst(), t2.getFirst()) > 0) {
                return new Triple<>(t2.getFirst(), t1.getThird() + 1, t1.getThird() + 1);
            } else {
                return new Triple<>(t1.getFirst(), t1.getSecond(), t1.getThird() + 1);
            }
        }), v -> v.getSecond() == -1L ? OptionalLong.empty() : OptionalLong.of(v.getSecond()));
    }

    @Override
    public <T> Collector<T, ?, OptionalLong> maxIndex(Comparator<? super T> comparator) {
        return minIndex(comparator.reversed());
    }
}
