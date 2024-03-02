package queue;

public class Node {
    private final Object el;
    private Node next;
    public Node(Object el, Node next) {
        this.el = el;
        this.next = next;
    }

    public Object getElement() {
        return el;
    }

    public Node getNext() {
        return next;
    }

    public void changeNext(Node next){
        this.next = next;
    }
}
