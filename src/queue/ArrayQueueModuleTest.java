package queue;

public class ArrayQueueModuleTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            ArrayQueueModule.enqueue("number " + i);
        }
        while(!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.size() + " " + ArrayQueueModule.element() + " " + ArrayQueueModule.dequeue());
        }
    }
}
