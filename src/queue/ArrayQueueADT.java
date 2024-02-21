package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n] (many queue)
// Inv: (for all queue) n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k, qu): forall in queue qu  i=1,...,k: a'[i] = a[i]
// Let: immutable(qu): for queue qu n' = n && forall  i=1,...,n: a'[i] = a[i]
public class ArrayQueueADT {
    private Object[] m;
    private int size;
    private int l, r;

    //pred: true
    //post: this.n' = 0 && for all other queue q immutable(q)
    public ArrayQueueADT() {
        m = new Object[2];
    }

    //pred: queue != null
    //post: n' = n && immutable(n, queue) && for all other queue q immutable(q)
    private static void expand(ArrayQueueADT queue) {
        assert queue != null;
        if (queue.size == queue.m.length) {
            Object[] newM = new Object[2 * queue.size];
            for (int i = 0; i < queue.size; ++i) {
                newM[i] = queue.m[(i + queue.l) % queue.m.length];
            }
            queue.m = newM;
            queue.l = 0;
            queue.r = queue.size;
        }
    }

    //pred: o != null && queue != null
    //post: n' = n + 1 && immutable(n, queue) && a[n'] = o && for all other queue q immutable(q)
    public static void enqueue(ArrayQueueADT queue, Object o) {
        assert queue != null && o != null;
        expand(queue);
        queue.m[queue.r] = o;
        queue.r = (queue.r + 1) % queue.m.length;
        queue.size++;
    }

    //pred: n != 0 && queue != null
    //post: n = n' && immutable(n, queue) && R = a[1] && for all other queue q immutable(q)
    public static Object element(ArrayQueueADT queue) {
        assert queue != null && queue.size != 0;
        return queue.m[queue.l];
    }

    //pred: n != 0 && queue != null
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1] && for all other queue q immutable(q)
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null && queue.size != 0;
        queue.size--;
        Object res = queue.m[queue.l];
        queue.m[queue.l] = null;
        queue.l = (queue.l + 1) % queue.m.length;
        return res;
    }

    //pred: queue != null
    //post: n = n' && immutable(n, queue) && R = n && for all other queue q immutable(q)
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    //queue != null
    //post: n = n' && immutable(n, queue) && R = (n == 0) && for all other queue q immutable(q)
    public  static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }

    //pred: queue != null
    //post: n' = 0 && for all other queue q immutable(q)
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.size = 0;
        queue.m = new Object[2];
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
        queue.size++;
    }

    //pred: n != 0 && queue != null
    //post: n = n' && immutable(n) && R = a[n] && for all other queue q immutable(q)
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null && queue.size != 0;
        return queue.m[(queue.r - 1 + queue.m.length) % queue.m.length];
    }

    //pred: n != 0 && queue != null
    //post: n' = n - 1 && immutable(n') && R = a[n] && for all other queue q immutable(q)
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null && queue.size != 0;
        queue.size--;
        queue.r = (queue.r - 1 + queue.m.length) % queue.m.length;
        Object res = queue.m[queue.r];
        queue.m[queue.r] = null;
        return res;
    }

    //pred: p != null && queue != null
    //post: n' = n && immutable(n) && R = min({i : p.test(a[i]) == true}), 1 <= i <= n && for all other queue q immutable(q)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> p) {
        assert p != null;
        int res = -1;
        for (int i = 0; i < queue.size; ++i) {
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
        assert p != null;
        int res = -1;
        for (int i = 0; i < queue.size; ++i) {
            if (p.test(queue.m[(i + queue.l) % queue.m.length])) {
                res = i;
            }
        }
        return res;
    }
}
