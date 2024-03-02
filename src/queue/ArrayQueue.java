package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1,...,k: a'[i] = a[i]
public class ArrayQueue extends AbstractQueue implements Queue {
    private Object[] m;

    private int l;

    //pred: true
    //post: n' = 0
    public ArrayQueue()  {
        m = new Object[10];
    }


    //pred: true
    //post: n' = n && immutable(n)
    private void expand() {
        if (size() == m.length - 1) {
            Object[] newM = new Object[2 * size()];
            System.arraycopy(m, l, newM, 0, m.length - l);
            if ((size + l) % m.length != m.length - 1) System.arraycopy(m, 0, newM, m.length - l,
                    (size + l) % m.length);
            m = newM;
            l = 0;
        }
    }

    public void enqueueAbs(Object o) {
        expand();
        m[(size + l) % m.length] = o;
    }

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[1]
    public Object element() {
        assert ((size + l) % m.length) != l;
        return m[l];
    }

    public Object dequeueAbs() {
        Object res = m[l];
        m[l] = null;
        l = (l + 1) % m.length;
        return res;
    }

    public void clear() {
        l = 0;
        m = new Object[10];
        size = 0;
    }

    //pred: o != null
    //post: n' = n + 1 && for i = 2..n' a'[i] = a[i - 1] && a[1] = 0
    public void push(Object o) {
        assert o != null;
        expand();
        l = (l - 1 + m.length) % m.length;
        m[l] = o;
        size++;
    }

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[n]
    public Object peek() {
        assert (size + l) % m.length != l;
        return m[(size + l - 1 + m.length) % m.length];
    }

    //pred: n != 0
    //post: n' = n - 1 && immutable(n') && R = a[n]
    public Object remove() {
        assert size != 0;
        Object res = m[(size + l - 1) % m.length];
        size--;
        m[(size + l) % m.length] = null;
        return res;
    }

    //pred: p != null
    //post: n' = n && immutable(n) && R = min({i : p.test(a[i]) == true}), 1 <= i <= n
    public int indexIf(Predicate<Object> p) {
        assert p != null;
        int res = -1;
        for (int i = 0; i < size(); ++i) {
            if (p.test(m[(i + l) % m.length])) {
                res = i;
                break;
            }
        }
        return res;
    }

    //pred: p != null
    //post: n' = n && immutable(n) && R = max({i : p.test(a[i]) == true}), 1 <= i <= n
    public int lastIndexIf(Predicate<Object> p) {
        assert p != null;
        int res = -1;
        for (int i = 0; i < size(); ++i) {
            if (p.test(m[(i + l) % m.length])) {
                res = i;
            }
        }
        return res;
    }


    protected ArrayQueue getNew() {
        return new ArrayQueue();
    }

    protected Object[] getValues() {
        Object[] values = new Object[size];
        System.arraycopy(m, l, values, 0, (l <= (size + l) % m.length ? size : m.length - l));
        if (l > (size + l) % m.length) System.arraycopy(m, 0, values, m.length - l, (size + l) % m.length);
        return values;
    }
}
