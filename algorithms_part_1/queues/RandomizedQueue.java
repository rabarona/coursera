/**
 * Created by Ricardo Barona on 2/4/17.
 * <p>
 * Execution: java RandomizedQueue
 * <p>
 * Dependencies algs4.jar
 * <p>
 * A randomized queue is similar to a stack or queue, except that the item
 * removed is chosen uniformly at random from items in the data structure.
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int numberItems = 0;
    private int tail = 0;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    public static void main(String[] args) {

        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

        for (int i = 0; i < 10; i++) {

            randomizedQueue.enqueue(0);
            randomizedQueue.enqueue(1);
            randomizedQueue.enqueue(2);
            randomizedQueue.enqueue(3);
            randomizedQueue.enqueue(4);

            StdOut.println(randomizedQueue.dequeue());
            StdOut.println(randomizedQueue.dequeue());
            StdOut.println(randomizedQueue.dequeue());
            StdOut.println(randomizedQueue.dequeue());
            StdOut.println(randomizedQueue.dequeue());

            StdOut.println("-----------------------");
        }

    }

    /**
     * Checks if the queue is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return numberItems == 0;
    }

    /**
     * Return the number of items on the queue
     *
     * @return
     */
    public int size() {
        return numberItems;
    }

    /**
     * Add a new item
     *
     * @param item
     */
    public void enqueue(Item item) {

        checkItemIsNull(item);

        if (tail == queue.length) {
            resize(2 * queue.length);
        }

        queue[tail] = item;
        numberItems++;
        tail++;
    }

    /**
     * Remove and return a random item
     *
     * @return
     */
    public Item dequeue() {

        throwErrorIfEmpty();

        if (numberItems > 0 && numberItems == queue.length / 4) {
            resize(queue.length / 2);
        }

        int randomIndex = getRandomIndex();
        Item item = queue[randomIndex];

        if (item == null) {
            resize(queue.length);
            item = queue[randomIndex];
        }

        queue[randomIndex] = null;
        numberItems--;

        return item;
    }

    /**
     * Return (but do not remove) a random item
     *
     * @return
     */
    public Item sample() {

        throwErrorIfEmpty();

        int randomIndex = getRandomIndex();
        Item item = queue[randomIndex];

        if (item == null) {
            resize(queue.length);
            item = queue[randomIndex];
        }

        return item;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private int getRandomIndex() {

        return StdRandom.uniform(0, numberItems);
    }

    /**
     * Given an existing array, will resize to a new capacity
     *
     * @param capacity The new capacity of the array
     */
    private void resize(int capacity) {

        Item[] copy = (Item[]) new Object[capacity];

        int i = 0;
        int j = 0;

        while (i < tail) {
            if (queue[i] != null) {
                copy[j] = queue[i];
                j++;
            }
            i++;
        }

        tail = numberItems;
        queue = copy;
    }

    /**
     * Checks if the item is null, if true, throws new NullPointerException
     *
     * @param item The new item to add
     */
    private void checkItemIsNull(Item item) {
        if (item == null) {
            throw new NullPointerException("Can't add a null item, I'm sorry.");
        }
    }

    /**
     * Throws NoSuchElementException if there are no more elements
     */
    private void throwErrorIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty " +
                    "already");
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int iteratorNumberItems = numberItems;
        private Item[] iteratorQueue;

        public RandomizedQueueIterator() {
            iteratorQueue = (Item[]) new Object[numberItems];

            int i = 0;
            int j = 0;

            while (i < tail) {
                if (queue[i] != null) {
                    iteratorQueue[j] = queue[i];
                    j++;
                }
                i++;
            }

            StdRandom.shuffle(iteratorQueue);
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return iteratorNumberItems > 0;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Item next() {
            if (iteratorNumberItems <= 0) {
                throw new NoSuchElementException("There are no more items.");
            }
            return iteratorQueue[--iteratorNumberItems];
        }
    }
}
