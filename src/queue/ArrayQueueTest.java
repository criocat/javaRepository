package queue;

public class ArrayQueueTest {

    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        for (int i = 0; i < 10; ++i) {
            q.enqueue("number " + i);
        }
        while(!q.isEmpty()) {
            System.out.println(q.size() + " " + q.element() + " " + q.dequeue());
        }
    }
}
