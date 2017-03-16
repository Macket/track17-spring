package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Queue, Stack {

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node first = null; // Первый элемент списка
    private Node last = null; // Последний элемент списка
    private int len = 0; // Длина списка

    @Override
    void add(int item) {
        if (first == null) {
            first = new Node(null, null, item);
            last = first;
        } else {
            last.next = new Node(last, null, item);
            last = last.next;
        }
        len++;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= len || idx < 0) {
            throw new NoSuchElementException();
        } else {
            // Поиск удаляемого элемента
            Node aimNode = first;
            for (int i = 0; i != idx; i++) {
                aimNode = aimNode.next;
            }
            // Удаление
            if (len == 1) {
                first = null;
            } else if (idx == 0) {
                first = first.next;
                first.prev = null;
            } else if (idx == len - 1) {
                last = last.prev;
                last.next = null;
            } else {
                aimNode.prev.next = aimNode.next;
                aimNode.next.prev = aimNode.prev;
            }
            len--;

            return aimNode.val;
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= len) {
            throw new NoSuchElementException();
        } else {
            // Поиск элемента
            Node aimNode = first;
            for (int i = 0; i != idx; i++) {
                aimNode = aimNode.next;
            }

            return aimNode.val;
        }
    }

    @Override
    int size() {
        return len;
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeue() {
        return remove(0);
    }

    @Override
    public void push(int value) {
        add(value);
    }

    @Override
    public int pop() {
        return remove(len - 1);
    }
}
