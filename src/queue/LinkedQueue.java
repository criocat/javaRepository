package queue;

public class LinkedQueue extends AbstractQueue implements Queue {
    private Node head, tail;
    public LinkedQueue() {
        head = null;
        tail = null;
    }

    public void enqueueAbs(Object o) {
        if (size == 0) {
            Node newNode = new Node(o, null);
            head = newNode;
            tail = newNode;
        }
        else {
            Node newTail = new Node(o, null);
            tail.changeNext(newTail);
            tail = newTail;
        }
    }

    public Object element() {
        assert size != 0;
        return head.getElement();
    }

    public Object dequeueAbs() {
        Object res = head.getElement();
        head = head.getNext();
        return res;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    protected LinkedQueue getNew() {
        return new LinkedQueue();
    }

    protected Object[] getValues() {
        Object[] values = new Object[size];
        Node p = head;
        for (int i = 0; i < size; ++i) {
            values[i] = p.getElement();
            p = p.getNext();
        }
        return values;
    }
}
