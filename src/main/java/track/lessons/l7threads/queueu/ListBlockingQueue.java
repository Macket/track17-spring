package track.lessons.l7threads.queueu;

import java.util.LinkedList;

/**
 * TODO: implements on lesson
 */

public class ListBlockingQueue<E> {

    public static final int DEFAULT_CAPACITY = 10;

    private static final Object monitor = new Object();

    private int capacity;
    private LinkedList<E> list = new LinkedList<>();

    public ListBlockingQueue() {
        capacity = DEFAULT_CAPACITY;
    }

    public ListBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /*
    Внутри критической секции пытаемся
    поместить элемент в очередь
    Если очередь полная isFull==true
    то блокируемся на wait() пока условие не будет выполнено
    */
    public synchronized void put(E elem) throws InterruptedException {
        while (this.isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.isEmpty()) {
            notify();
        }
        list.addLast(elem);
    }

    /*
    Внутри критической секции пытаемся
    достать элемент из очереди
    Если очередь пустая isEmpty == true
    то блокируемся на wait() пока условие
    не будет выполнено
    */
    public synchronized E take() throws InterruptedException {
        while (this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.isFull()) {
            notify();
        }
        return list.removeLast();
    }

    public synchronized E poll() throws InterruptedException {
        if (this.isEmpty()) {
            return null;
        } else {
            notify();
            return list.removeLast();
        }
    }

    public synchronized boolean offer(E elem) throws InterruptedException {
        if (this.isFull()) {
            return false;
        } else {
            list.addLast(elem);
            notify();
            return true;
        }
    }

    private boolean isFull() {
        return list.size() == capacity;
    }

    private boolean isEmpty() {
        return list.size() == 0;
    }

    public int getSize() {
        return list.size();
    }
}
