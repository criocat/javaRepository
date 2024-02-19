package queue;

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
        assert o != null;
        expand(queue);
        queue.m[queue.r] = o;
        queue.r = (queue.r + 1) % queue.m.length;
        queue.size++;
    }

    //pred: n != 0 && queue != null
    //post: n = n' && immutable(n, queue) && R = a[1] && for all other queue q immutable(q)
    public static Object element(ArrayQueueADT queue) {
        return queue.m[queue.l];
    }

    //pred: n != 0 && queue != null
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1] && for all other queue q immutable(q)
    public static Object dequeue(ArrayQueueADT queue) {
        queue.size--;
        Object res = queue.m[queue.l];
        queue.m[queue.l] = null;
        queue.l = (queue.l + 1) % queue.m.length;
        return res;
    }

    //pred: queue != null
    //post: n = n' && immutable(n, queue) && R = n && for all other queue q immutable(q)
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    //queue != null
    //post: n = n' && immutable(n, queue) && R = (n == 0) && for all other queue q immutable(q)
    public  static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    //pred: queue != null
    //post: n' = 0 && for all other queue q immutable(q)
    public static void clear(ArrayQueueADT queue) {
        queue.size = 0;
        queue.m = new Object[2];
        queue.l = 0;
        queue.r = 0;
    }
}
