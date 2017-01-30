/**
 * Created by Ricardo Barona on 1/28/17
 * <p>
 * Execution: java Percolation n t
 * Where n will create a n-by-n grid and t is the number of times
 * Percolate program will execute.
 * <p>
 * Dependencies Percolation.java, algs4.jar
 * <p>
 * This program performs T independent computational experiments
 * on an n-by-n grid, and prints the sample mean, sample standard deviation,
 * and the 95% confidence interval for the percolation threshold.
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private double[] thresholds;
    private int n;
    private int trials;

    public PercolationStats(int n, int trials) {

        validateInput(n);
        validateInput(trials);

        this.thresholds = new double[trials];
        this.n = n;
        this.trials = trials;
        double sites = (double) this.n * this.n;
        int a = 1;      // uniform a - the left endpoint
        int b = n + 1;  // uniform b - the right endpoint

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n); // new Percolation instance

            while (!p.percolates()) {
                int row = StdRandom.uniform(a, b);
                int col = StdRandom.uniform(a, b);
                p.open(row, col);
            }
            thresholds[i] = (double) p.numberOfOpenSites() / sites;
        }
    }

    // test client (described below)
    public static void main(String[] args) {

        int n;
        int trials;

        if (!(args.length > 1))
            throw new IllegalArgumentException("Not enough" +
                    " arguments");

        try {
            n = Integer.parseInt(args[0]);      // n-by-n percolation system
            trials = Integer.parseInt(args[1]); // number of trials
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input parameters should be " +
                    "2 numbers bigger than 0");
        }


        PercolationStats percolationStats =
                new PercolationStats(n, trials); // new Percolation instance

        StdOut.println("mean:                       " + percolationStats.mean());
        StdOut.println("stddev:                     " + percolationStats.stddev());
        StdOut.println("95% confidence interval:    " + percolationStats
                .confidenceLo() + " " + percolationStats.confidenceHi());

    }

    /**
     * Calculates sample mean of percolation threshold
     *
     * @return
     */
    public double mean() {

        return StdStats.mean(thresholds);
    }

    /**
     * Calculates sample standard deviation of percolation threshold
     *
     * @return
     */
    public double stddev() {

        return StdStats.stddev(thresholds);
    }

    /**
     * Calculates low  endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceLo() {

        double mean = mean();
        double stddev = stddev();

        return (mean - (1.96 * (stddev) / Math.sqrt(this.trials)));
    }

    /**
     * Calculates high endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceHi() {

        double mean = mean();
        double stddev = stddev();

        return (mean + (1.96 * (stddev) / Math.sqrt(this.trials)));
    }

    /**
     * Validates the n-by-n size is bigger than 0
     *
     * @param input a number bigger than 0
     */
    private void validateInput(int input) {
        if (input <= 0) throw new IllegalArgumentException("Invalid input " +
                input + ". Make sure to provide a value bigger than 0");
    }
}
