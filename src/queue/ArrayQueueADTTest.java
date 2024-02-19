package queue;

public class ArrayQueueADTTest {

    public static void main(String[] args) {
        ArrayQueueADT q = new ArrayQueueADT();
        for (int i = 0; i < 10; ++i) {
            ArrayQueueADT.enqueue(q, "number " + i);
        }
        while(!ArrayQueueADT.isEmpty(q)) {
            System.out.println(ArrayQueueADT.size(q) + " " + ArrayQueueADT.element(q) + " " + ArrayQueueADT.dequeue(q));
        }
    }
}
