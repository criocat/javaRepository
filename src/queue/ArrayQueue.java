package queue;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1,...,k: a'[i] = a[i]
public class ArrayQueue {
    private Object[] m;;
    private int size;
    private int l, r;

    //pred: true
    //post: n' = 0
    public ArrayQueue() {
        m = new Object[2];
    }


    //pred: true
    //post: n' = n && immutable(n)
    private void expand() {
        if (size == m.length) {
            Object[] newM = new Object[2 * size];
            for (int i = 0; i < size; ++i) {
                newM[i] = m[(i + l) % m.length];
            }
            m = newM;
            l = 0;
            r = size;
        }
    }

    //pred: o != null
    //post: n' = n + 1 && immutable(n) && a[n'] = o
    public void enqueue(Object o) {
        assert o != null;
        expand();
        m[r] = o;
        r = (r + 1) % m.length;
        size++;
    }

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[1]
    public Object element() {
        assert size != 0;
        return m[l];
    }

    //pred: n != 0
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1]
    public Object dequeue() {
        size--;
        Object res = m[l];
        m[l] = null;
        l = (l + 1) % m.length;
        return res;
    }

    //pred: true
    //post: n = n' && immutable(n) && R = n
    public int size() {
        return size;
    }

    //pred: true
    //post: n = n' && immutable(n) && R = (n == 0)
    public boolean isEmpty() {
        return size == 0;
    }

    //pred: true
    //post: n' = 0
    public void clear() {
        size = 0;
        m = new Object[2];
        l = 0;
        r = 0;
    }
}
