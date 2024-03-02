package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n] (many queue)
// Inv: (for all queue) n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k, qu): forall in queue qu  i=1,...,k: a'[i] = a[i]
// Let: immutable(qu): for queue qu n' = n && forall  i=1,...,n: a'[i] = a[i]
public class ArrayQueueADT {
    private Object[] m;
    private int l, r;

    //pred: true
    //post: this.n' = 0 && for all other queue q immutable(q)
    public ArrayQueueADT() {
        m = new Object[10];
    }

    //pred: queue != null
    //post: n' = n && immutable(n, queue) && for all other queue q immutable(q)
    private static void expand(ArrayQueueADT queue) {
        if (size(queue) == queue.m.length - 1) {
            Object[] newM = new Object[2 * size(queue)];
            System.arraycopy(queue.m, queue.l, newM, 0, queue.m.length - queue.l);
            if (queue.r != queue.m.length - 1) System.arraycopy(queue.m, 0, newM, queue.m.length - queue.l, queue.r);
            queue.r = queue.m.length - 1;
            queue.m = newM;
            queue.l = 0;
        }
    }

    //pred: o != null && queue != null
    //post: n' = n + 1 && immutable(n, queue) && a[n'] = o && for all other queue q immutable(q)
    public static void enqueue(ArrayQueueADT queue, Object o) {
        assert queue != null && o != null;
        expand(queue);
        queue.m[queue.r] = o;
        queue.r = (queue.r + 1) % queue.m.length;
    }

    //pred: n != 0 && queue != null
    //post: n = n' && immutable(n, queue) && R = a[1] && for all other queue q immutable(q)
    public static Object element(ArrayQueueADT queue) {
        assert queue != null && queue.l != queue.r;
        return queue.m[queue.l];
    }

    //pred: n != 0 && queue != null
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1] && for all other queue q immutable(q)
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null && queue.l != queue.r;
        Object res = queue.m[queue.l];
        queue.m[queue.l] = null;
        queue.l = (queue.l + 1) % queue.m.length;
        return res;
    }

    //pred: queue != null
    //post: n = n' && immutable(n, queue) && R = n && for all other queue q immutable(q)
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return (queue.r -  queue.l + queue.m.length) % queue.m.length;
    }

    //queue != null
    //post: n = n' && immutable(n, queue) && R = (n == 0) && for all other queue q immutable(q)
    public  static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.l == queue.r;
    }

    //pred: queue != null
    //post: n' = 0 && for all other queue q immutable(q)
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.m = new Object[10];
        queue.l = 0;
        queue.r = 0;
    }

    //pred: o != null && queue != null
    //post: n' = n + 1 && for i = 2..n' a'[i] = a[i - 1] && a[1] = 0 && for all other queue q immutable(q)
    public static void push(ArrayQueueADT queue, Object o) {
        assert queue != null && o != null;
        expand(queue);
        queue.l = (queue.l - 1 + queue.m.length) % queue.m.length;
        queue.m[queue.l] = o;
    }

    //pred: n != 0 && queue != null
    //post: n = n' && immutable(n) && R = a[n] && for all other queue q immutable(q)
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null && queue.l != queue.r;
        return queue.m[(queue.r - 1 + queue.m.length) % queue.m.length];
    }

    //pred: n != 0 && queue != null
    //post: n' = n - 1 && immutable(n') && R = a[n] && for all other queue q immutable(q)
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null && queue.l != queue.r;
        queue.r = (queue.r - 1 + queue.m.length) % queue.m.length;
        Object res = queue.m[queue.r];
        queue.m[queue.r] = null;
        return res;
    }

    //pred: p != null && queue != null
    //post: n' = n && immutable(n) && R = min({i : p.test(a[i]) == true}), 1 <= i <= n && for all other queue q immutable(q)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> p) {
        assert p != null && queue != null;
        int res = -1;
        for (int i = 0; i < size(queue); ++i) {
            if (p.test(queue.m[(i + queue.l) % queue.m.length])) {
                res = i;
                break;
            }
        }
        return res;
    }

    //pred: p != null && queue != null
    //post: n' = n && immutable(n) && R = max({i : p.test(a[i]) == true}), 1 <= i <= n && for all other queue q immutable(q)
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> p) {
        assert p != null && queue != null;
        int res = -1;
        for (int i = 0; i < size(queue); ++i) {
            if (p.test(queue.m[(i + queue.l) % queue.m.length])) {
                res = i;
            }
        }
        return res;
    }
}
