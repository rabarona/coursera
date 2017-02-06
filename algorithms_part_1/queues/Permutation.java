/**
 * Created by Ricardo Barona on 2/4/17.
 * <p>
 * Execution: Permutation K < file.txt
 * <p>
 * Where k: is the size of the subset to print
 * and file.txt: contains a sequence of elements
 * <p>
 * Dependencies algs4.jar
 * <p>
 * Permutation, program that takes a command-line integer k; reads in a sequence
 * of strings from standard input using StdIn.readString(); and prints
 * exactly k of them, uniformly at random.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {

        int k;
        if (args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments");
        }

        try {
            k = Integer.parseInt(args[0]);      // size of subset
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input parameter should be " +
                    "a number");
        }

        // A new RandomizedQueue object
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }

        // A new Iterator of randomizedQueue
        Iterator<String> randomizedQueueIterator = randomizedQueue.iterator();

        int i = 0; // counter
        while (i < k && randomizedQueueIterator.hasNext()) {
            StdOut.println(randomizedQueueIterator.next());
            i++;
        }
    }
}
