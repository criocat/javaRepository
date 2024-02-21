package queue;

public class ArrayQueueBasicTest {

    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        System.out.println("test: enqueue, element, dequeue, size, isEmpty");
        for (int i = 0; i < 10; ++i) {
            q.enqueue("number " + i);
        }
        while(!q.isEmpty()) {
            System.out.println(q.size() + " " + q.element() + " " + q.dequeue());
        }
        System.out.println("test: push, remove, peek");
        for (int i = 0; i < 10; ++i) {
            q.push("number " + i);
        }
        while (!q.isEmpty()) {
            System.out.println(q.size() + " " + q.peek() + " " + q.remove());
        }
    }
}
