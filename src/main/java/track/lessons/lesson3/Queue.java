package track.lessons.lesson3;

/**
 * Created by ivan on 16.03.17.
 */
public interface Queue {

    void enqueue(int value); // поместить элемент в очередь

    int dequeue(); // вытащить первый элемент из очереди
}
