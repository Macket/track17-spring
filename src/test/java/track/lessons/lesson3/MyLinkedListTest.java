package track.lessons.lesson3;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MyLinkedListTest {

    @Test(expected = NoSuchElementException.class)
    public void emptyList() throws Exception {
        List list = new MyLinkedList();
        Assert.assertTrue(list.size() == 0);
        list.get(0);
    }

    @Test
    public void listAdd() throws Exception {
        List list = new MyLinkedList();
        list.add(1);

        Assert.assertTrue(list.size() == 1);
    }

    @Test
    public void listAddRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        list.remove(1);
        Assert.assertEquals(3, list.get(1));
        Assert.assertEquals(1, list.get(0));

        list.remove(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void stackPush(){
        MyLinkedList stack = new MyLinkedList();
        stack.push(1);

        Assert.assertTrue(stack.size() == 1);
    }

    @Test
    public void stackPop(){
        MyLinkedList stack = new MyLinkedList();
        stack.push(1);
        stack.pop();

        Assert.assertTrue(stack.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyStack() throws Exception {
        MyLinkedList stack = new MyLinkedList();
        Assert.assertTrue(stack.size() == 0);
        stack.pop();
    }

    @Test
    public void stackPushPop() throws Exception {
        MyLinkedList stack = new MyLinkedList();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        Assert.assertEquals(3, stack.size());

        Assert.assertEquals(3, stack.pop());
        Assert.assertEquals(2, stack.pop());
        Assert.assertEquals(1, stack.pop());

        Assert.assertTrue(stack.size() == 0);
    }

    @Test
    public void queueEnqueue(){
        MyLinkedList queue = new MyLinkedList();
        queue.enqueue(1);

        Assert.assertTrue(queue.size() == 1);
    }

    @Test
    public void queueDequeue(){
        MyLinkedList queue = new MyLinkedList();
        queue.enqueue(1);
        queue.dequeue();

        Assert.assertTrue(queue.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyQueue() throws Exception {
        MyLinkedList queue = new MyLinkedList();
        Assert.assertTrue(queue.size() == 0);
        queue.dequeue();
    }

    @Test
    public void queueEnqueueDequeue() throws Exception {
        MyLinkedList queue = new MyLinkedList();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(3, queue.size());

        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(2, queue.dequeue());
        Assert.assertEquals(3, queue.dequeue());

        Assert.assertTrue(queue.size() == 0);
    }
}