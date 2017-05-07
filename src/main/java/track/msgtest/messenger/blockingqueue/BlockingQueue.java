package track.msgtest.messenger.blockingqueue;

import java.util.Iterator;
import java.util.LinkedList;

public class BlockingQueue<E> {


    private LinkedList<E> list = new LinkedList<>();

    public synchronized void put(E elem) throws InterruptedException {
        if (this.isEmpty()) {
            notifyAll();
        }
        list.addLast(elem);
    }

    public synchronized E take() throws InterruptedException {
        while (this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list.removeFirst();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public int getSize() {
        return list.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
        }
        return new String(stringBuilder);
    }
}
