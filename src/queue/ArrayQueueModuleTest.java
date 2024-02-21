package queue;

public class ArrayQueueModuleTest {

    public static void main(String[] args) {
        System.out.println("test: enqueue, element, dequeue, size, isEmpty");
        for (int i = 0; i < 10; ++i) {
            ArrayQueueModule.enqueue("number " + i);
        }
        while(!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.size() + " " + ArrayQueueModule.element() + " " + ArrayQueueModule.dequeue());
        }
        System.out.println("test: push, remove, peek");
        for (int i = 0; i < 10; ++i) {
            ArrayQueueModule.push("number " + i);
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.size() + " " + ArrayQueueModule.peek() + " " + ArrayQueueModule.remove());
        }
    }
}
