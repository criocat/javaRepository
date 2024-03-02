package queue;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class AbstractQueue {
    protected int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void enqueueAbs(Object o);

    //pred: o != null
    //post: n' = n + 1 && immutable(n) && a[n'] = o
    public void enqueue(Object o) {
        assert o != null;
        enqueueAbs(o);
        size++;
    }



    public abstract Object element();

    //pred: n != 0
    //post: n' = n - 1 && forall i = 1,...,n' a'[i] = a[i + 1] && R = a[1]
    protected abstract Object dequeueAbs();

    public Object dequeue(){
        assert size != -1;
        size--;
        return dequeueAbs();
    }

    public abstract void clear();

    protected abstract AbstractQueue getNew();

    protected abstract Object[] getValues();

    public Queue flatMap(Function<Object, List<Object>> f) {
        assert f != null;
        Object[] values = getValues();
        AbstractQueue newQueue = getNew();
        for (int i = 0; i < size; ++i) {
            List<Object> curList = f.apply(values[i]);
            for (Object o : curList) {
                newQueue.enqueue(o);
            }
        }
        return (Queue) newQueue;
    }


    public Object reduce(Object init, BinaryOperator<Object> bo) {
        assert init != null && bo != null;
        Object[] values = getValues();
        Object last = init;
        for (int i = 0; i < size; ++i) {
            last = bo.apply(last, values[i]);
        }
        return last;
    }
}
