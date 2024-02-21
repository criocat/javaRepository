package queue;

public class ArrayQueueADTTest {

    public static void main(String[] args) {
        ArrayQueueADT q = new ArrayQueueADT();
        System.out.println("test: enqueue, element, dequeue, size, isEmpty");
        for (int i = 0; i < 10; ++i) {
            ArrayQueueADT.enqueue(q, "number " + i);
        }
        while(!ArrayQueueADT.isEmpty(q)) {
            System.out.println(ArrayQueueADT.size(q) + " " + ArrayQueueADT.element(q) + " " + ArrayQueueADT.dequeue(q));
        }
        System.out.println("test: push, remove, peek");
        for (int i = 0; i < 10; ++i) {
            ArrayQueueADT.push(q, "number " + i);
        }
        while (!ArrayQueueADT.isEmpty(q)) {
            System.out.println(ArrayQueueADT.size(q) + " " + ArrayQueueADT.peek(q) + " " + ArrayQueueADT.remove(q));
        }
    }
}
