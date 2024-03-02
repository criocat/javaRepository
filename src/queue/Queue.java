package queue;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1,...,k: a'[i] = a[i]
public interface Queue {

    //pred: o != null
    //post: n' = n + 1 && immutable(n) && a[n'] = o
    void enqueue(Object o);

    //pred: n != 0
    //post: n = n' && immutable(n) && R = a[1]
    Object element();

    //pred: n != 0
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1]
    Object dequeue();

    //pred: true
    //post: n = n' && immutable(n) && R = n
    int size();

    //pred: true
    //post: n = n' && immutable(n) && R = (n == 0)
    boolean isEmpty();

    //pred: true
    //post: n' = 0
    void clear();

    //pred: f != null
    //post: {
    // partSize(l) = sum(i = 1...l) f(a[i]).length, l > 0; 0, l == 0
    // equals(mas1, mas2, l2) : for (i = 1... mas1.length) mas1[i] = mas2[l2 + i]
    //  n = n' && immutable(n) &&  R = Queue newq : newq.n' = partSize(n) &&
    //  for (i = 1...n) equals(f(a[i]), newq, partSize(l))
    // }
    Queue flatMap(Function<Object, List<Object>> f);

    //pred: init != null &&  b != null
    // post: n = n' && immutable(n) && R = d[n], d[0] = init, for i = 1..n : d[i] = t.apply(d[i - 1], a[i])
    Object reduce(Object init, BinaryOperator<Object> b);
}
