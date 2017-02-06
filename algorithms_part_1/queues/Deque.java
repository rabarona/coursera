/**
 * Created by Ricardo Barona on 2/2/17.
 * <p>
 * Execution: java Dequeue
 * <p>
 * Dependencies algs4.jar
 * <p>
 * A double-ended queue or deque (pronounced "deck") is a generalization of a
 * stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int numberItems = 0;

    public Deque() {
        first = null;
        last = null;
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);

        Iterator<Integer> iterator = deque.iterator();

        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }

    }

    /**
     * Checks if the queue is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items on the Deque object
     *
     * @return
     */
    public int size() {

        return numberItems;
    }

    /**
     * Add a new item to the front
     *
     * @param item The new item to add
     */
    public void addFirst(Item item) {

        checkItemIsNull(item);

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (oldFirst != null) oldFirst.prev = first;
        if (last == null) last = first;
        else if (last.prev == null) last.prev = first;
        numberItems++;
    }

    /**
     * Add a new item to the end
     *
     * @param item The new item to add
     */
    public void addLast(Item item) {

        checkItemIsNull(item);

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        numberItems++;
    }

    /**
     * Removes and return the item from the front
     *
     * @return
     */
    public Item removeFirst() {

        throwErrorIfEmpty();

        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        if (isEmpty()) last = null;
        numberItems--;
        return item;
    }

    /**
     * Removes and return the item from the end
     *
     * @return
     */
    public Item removeLast() {

        throwErrorIfEmpty();

        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        if (last == null) first = last;
        numberItems--;
        return item;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    /**
     * Checks if the item is null, if true, throws new NullPointerException
     *
     * @param item The new item to add
     */
    private void checkItemIsNull(Item item) {
        if (item == null) {
            throw new NullPointerException("Can't add a null item, I'm sorry");
        }
    }

    /**
     * Throws NoSuchElementException if there are no more elements
     */
    private void throwErrorIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty already");
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("There are no more items.");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

}