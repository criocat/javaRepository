package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1,...,k: a'[i] = a[i]
public class ArrayQueue {
    private Object[] m;

    private int l, r;

    //pred: true
    //post: n' = 0
    public ArrayQueue() {
        m = new Object[10];
    }


    //pred: true
    //post: n' = n && immutable(n)
    private void expand() {
        if (size == m.length) {
            Object[] newM = new Object[2 * size];
            if (l < r) {
                System.arraycopy(m, l, newM, 0, r - l);
            } else {
                System.arraycopy(m, l, newM, 0, m.length - l);
                System.arraycopy(m, 0, newM, m.length - l, r);
            }
            r = m.length;
            m = newM;
            l = 0;
        }
    }

    //pred: o != null
    //post: n' = n + 1 && immutable(n) && a[n'] = o
    public void enqueue(Object o) {
        assert o != null;
        expand();
        m[r] = o;
        r = (r + 1) % m.length;
    }

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[1]
    public Object element() {
        assert r != l;
        return m[l];
    }

    //pred: n != 0
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1]
    public Object dequeue() {
        assert r != l;
        Object res = m[l];
        m[l] = null;
        l = (l + 1) % m.length;
        return res;
    }

    //pred: true
    //post: n = n' && immutable(n) && R = n
    public int size() {
        return r - l;
    }

    //pred: true
    //post: n = n' && immutable(n) && R = (n == 0)
    public boolean isEmpty() {
        return r == l;
    }

    //pred: true
    //post: n' = 0
    public void clear() {
        r = 0;
        l = 0;
        m = new Object[10];
    }

    //pred: o != null
    //post: n' = n + 1 && for i = 2..n' a'[i] = a[i - 1] && a[1] = 0
    public void push(Object o) {
        assert o != null;
        expand();
        l = (l - 1 + m.length) % m.length;
        m[l] = o;
    }

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[n]
    public Object peek() {
        assert r != l;
        return m[(r - 1 + m.length) % m.length];
    }

    //pred: n != 0
    //post: n' = n - 1 && immutable(n') && R = a[n]
    public Object remove() {
        assert r != l;
        r = (r - 1 + m.length) % m.length;
        Object res = m[r];
        m[r] = null;
        return res;
    }

    //pred: p != null
    //post: n' = n && immutable(n) && R = min({i : p.test(a[i]) == true}), 1 <= i <= n
    public int indexIf(Predicate<Object> p) {
        assert p != null;
        int res = -1;
        for (int i = 0; i < size; ++i) {
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
        for (int i = 0; i < size; ++i) {
            if (p.test(m[(i + l) % m.length])) {
                res = i;
            }
        }
        return res;
    }

}
