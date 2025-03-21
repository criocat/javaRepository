package info.kgeorgiy.ja.razinkov.arrayset;

import java.util.*;

import info.kgeorgiy.java.advanced.arrayset.AdvancedSet;


public class ArraySet<E> extends AbstractSet<E> implements AdvancedSet<E> {
    private List<E> list;
    private Comparator<? super E> comp;

    public ArraySet() {
        comp = null;
        list = new ArrayList<>();
    }

    public ArraySet(Collection<? extends E> coll, Comparator<? super E> comparator) {
        comp = comparator;
        TreeSet<E> tempset = new TreeSet<>(this::compare);
        tempset.addAll(coll);
        list = Collections.unmodifiableList(new ArrayList<>(tempset));
    }

    public ArraySet(Collection<? extends E> coll) {
        this(coll, null);
    }

    private E takeNearElement(E e, boolean inclusive, int delta) {
        int pos = binarySearch(e);
        if (pos != size() && inclusive && compare(list.get(pos), e) == 0) pos++;
        return pos + delta >= size() || pos + delta < 0 ? null : list.get(pos + delta);
    }

    @Override
    public E lower(E e) {
        return takeNearElement(e, false, -1);
    }

    @Override
    public E floor(E e) {
        return takeNearElement(e, true, -1);
    }

    @Override
    public E ceiling(E e) {
        return takeNearElement(e, false, 0);
    }

    @Override
    public E higher(E e) {
        return takeNearElement(e, true, 0);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return getListNoSort(list.reversed(), Collections.reverseOrder(comp));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return list.reversed().iterator();
    }

    @Override
    public ArraySet<E> subSet(E left, boolean leftInclusive, E right, boolean rightInclusive) {
        if (compare(left, right) > 0) throw new IllegalArgumentException("left element is greater than right element");
        int leftInd = binarySearch(left);
        int rightInd = binarySearch(right);
        if (leftInd != size() && !leftInclusive && compare(left, list.get(leftInd)) == 0) leftInd++;
        if (rightInd != size() && rightInclusive && compare(right, list.get(rightInd)) == 0) rightInd++;
        if (leftInd > rightInd) leftInd = rightInd = 0;
        return getListNoSort(list.subList(leftInd, rightInd), comp);
    }


    @Override
    public ArraySet<E> headSet(E e, boolean inclusive) {
        if (isEmpty() || compare(e, first()) < 0) return new ArraySet<>(Collections.emptyList(), comp);
        return subSet(first(), true, e, inclusive);
    }

    @Override
    public ArraySet<E> tailSet(E e, boolean inclusive) {
        if (isEmpty() || compare(e, last()) > 0) return new ArraySet<>(Collections.emptyList(), comp);
        return subSet(e, inclusive, last(), true);
    }

    @Override
    public int size() {
        return list.size();
    }


    @Override
    public Comparator<? super E> comparator() {
        return comp;
    }

    @Override
    public ArraySet<E> subSet(E left, E right) {
        return subSet(left, true, right, false);
    }

    @Override
    public ArraySet<E> headSet(E e) {
        return headSet(e, false);
    }

    @Override
    public ArraySet<E> tailSet(E e) {
        return tailSet(e, true);
    }

    @Override
    public <V> Map<E, V> asMap(V value) {
        return new AbstractMap<E, V>() {
            @Override
            public Set<Entry<E, V>> entrySet() {
                return new EntrySet<E, V>(ArraySet.this, value);
            }

            @Override
            public boolean containsKey(Object key) {
                return ArraySet.this.contains(key);
            }

            @Override
            public boolean containsValue(Object obj) {
                if (ArraySet.this.isEmpty()) return false;
                return Objects.equals(value, obj);
            }

            @Override
            public V get(Object key) {
                if (ArraySet.this.contains(key)) return value;
                else return null;
            }
        };
    }

    @Override
    public E first() {
        return list.getFirst();
    }

    @Override
    public E last() {
        return list.getLast();
    }

    @Override
    public boolean contains(Object e) {
        int pos = binarySearch(e);
        return pos != size() && compare(e, list.get(pos)) == 0;
    }

    private int binarySearch(Object e) {
        int res = Collections.binarySearch(list, e, this::compare);
        if (res < 0) res = -res - 1;
        return res;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object e1, Object e2) {
        // :NOTE: Huh? No, still unchecked. Either rewrite without, or explain why it's okay.
        if (comp == null) return ((Comparable<E>) e1).compareTo((E)e2);
        return comp.compare((E)e1, (E)e2);
    }

    private ArraySet<E> getListNoSort(List<E> lst, Comparator<? super E> comp) {
        ArraySet<E> res = new ArraySet<>();
        res.list = lst;
        res.comp = comp;
        return res;
    }
}
