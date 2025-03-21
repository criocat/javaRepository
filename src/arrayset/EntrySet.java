package info.kgeorgiy.ja.razinkov.arrayset;

import java.util.*;


public class EntrySet<E, V> extends AbstractSet<Map.Entry<E, V>> {
    private final Set<E> set;
    private final V value;

    public EntrySet(Set<E> set, V value) {
        this.set = set;
        this.value = value;
    }

    public Iterator<Map.Entry<E, V>> iterator() {
        Iterator<E> it = set.iterator();

        return new Iterator<>() {

            public boolean hasNext() {
                return it.hasNext();
            }

            public Map.Entry<E, V> next() {
                return new AbstractMap.SimpleEntry<>(it.next(), value);
            }
        };
    }

    public int size() {
        return set.size();
    }

    public boolean contains(Object obj) {
        return set.contains(((Map.Entry<?, ?>) obj).getKey());
    }
};