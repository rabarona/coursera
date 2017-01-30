/**
 * Created by Ricardo Barona on 1/28/17
 * <p>
 * Execution: java Percolation n
 * where n will create a n-by-n grid.
 * <p>
 * Dependencies algs4.jar
 * <p>
 * This program takes a n value and creates a grid of n * n of blocked
 * sites. Then it starts to open sites randomly until the grid percolates.
 * When it does, it prints the number of open sites.
 * <p>
 * <p>
 * A percolation system using an n-by-n grid of sites. Each site is either
 * open or blocked. A full site is an open site that can be connected to an
 * open site in the top row via a chain of neighboring (left, right, up, down)
 * open sites. We say the system percolates if there is a full site in the
 * bottom row. In other words, a system percolates if we fill all open sites
 * connected to the top row and that process fills some open site on the bottom
 * row.
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {

    private WeightedQuickUnionUF weightedQuickUnionUF;
    private WeightedQuickUnionUF weightedQuickUnionUFBackWash;
    private boolean[] openSites;
    private int n;
    private int countOpened;

    public Percolation(int n) {

        validateInput(n);

        this.n = n;
        this.countOpened = 0;
        this.openSites = initBlockedSites();
        this.weightedQuickUnionUF =
                new WeightedQuickUnionUF((n * n) + 2);
        this.weightedQuickUnionUFBackWash =
                new WeightedQuickUnionUF((n * n) + 1);
    }

    /**
     * Main method for Percolation execution, mostly test
     *
     * @param args input arguments for Percolation program. Only 1 parameter,
     *             the n-by-n grid size.
     */
    public static void main(String[] args) {

        int n;

        if (!(args.length > 0))
            throw new IllegalArgumentException("Not enough" +
                    " arguments");

        try {
            n = Integer.parseInt(args[0]);      // n-by-n percolation system
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input parameters should be " +
                    "2 numbers bigger than 0");
        }

        int a = 1;     // uniform a - the left endpoint
        int b = n + 1; // uniform b - the right endpoint

        Percolation p = new Percolation(n); // new Percolation instance

        while (!p.percolates()) {
            int row = StdRandom.uniform(a, b);
            int col = StdRandom.uniform(a, b);
            p.open(row, col);
        }

        StdOut.println("Number open sites: " + p.numberOfOpenSites());
    }

    /**
     * Opens a site in the given row-col
     *
     * @param row a number between 1 and n
     * @param col a number between 1 and n
     */
    public void open(int row, int col) {

        validateInputRowCol(row, col);

        int id = getOneDimensionIndex(row, col);
        int[] adjacentOpenSites;

        if (!isOpen(row, col)) {
            adjacentOpenSites = getAdjacentOpenSites(row, col);
            this.openSites[((n * (row - 1)) + (row - 1)) + col] = true;
            this.countOpened++;

            for (int i = 0; i < adjacentOpenSites.length; i++) {
                weightedQuickUnionUF.union(id, adjacentOpenSites[i]);
                if (adjacentOpenSites[i] != (this.n * this.n) + 1) {
                    weightedQuickUnionUFBackWash.union(id,
                            adjacentOpenSites[i]);
                }
            }
        }
    }

    /**
     * Checks if a site is already open
     *
     * @param row a number between 1 and n
     * @param col a number between 1 and n
     * @return
     */
    public boolean isOpen(int row, int col) {

        validateInputRowCol(row, col);

        return this.openSites[((n * (row - 1)) + (row - 1)) + col];
    }

    /**
     * Checks if a site is an open site that can be connected to an open
     * site in the top row
     *
     * @param row a number between 1 and n
     * @param col a number between 1 and n
     * @return
     */
    public boolean isFull(int row, int col) {

        validateInputRowCol(row, col);

        int id = getOneDimensionIndex(row, col);

        if (!isOpen(row, col)) return false;

        if (weightedQuickUnionUFBackWash.connected(id, (this.n * this.n)))
            return true;

        return false;
    }

    /**
     * Returns the number of open sites
     *
     * @return
     */
    public int numberOfOpenSites() {
        return this.countOpened;
    }

    /**
     * Return true when it percolates else false
     *
     * @return
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected((this.n * this.n),
                (this.n * this.n) + 1);
    }

    /**
     * Validates the range of row and col. Throw execption if they are out of
     * range
     *
     * @param row
     * @param col
     */
    private void validateInputRowCol(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new java.lang.IndexOutOfBoundsException("Invalid range: " +
                    "invalid row number " + row);
        } else if (col < 1 || col > this.n) {
            throw new java.lang.IndexOutOfBoundsException("Invalid range: " +
                    "invalid column " + col);
        }
    }

    /**
     * Validates the n-by-n size is bigger than 0
     *
     * @param input the n-by-n size
     */
    private void validateInput(int input) {
        if (input <= 0) throw new IllegalArgumentException("Invalid input " +
                input + ". Make sure to provide a value bigger than 0");
    }

    /**
     * Returns the index in a one dimension structure given row and col
     *
     * @param row
     * @param col
     * @return
     */
    private int getOneDimensionIndex(int row, int col) {

        return ((row - 1) * (this.n)) + (col - 1);
    }

    /**
     * Initializes an array with blocked sites
     *
     * @return
     */
    private boolean[] initBlockedSites() {

        int counter = 0;
        int xSeparator = 0;
        int arraySize = (n * n) + n;
        boolean[] result = new boolean[arraySize];

        do {
            result[counter] = false;

            if (xSeparator < n) {
                xSeparator++;
            } else {
                xSeparator = 0;
            }

            counter++;
        } while (counter < arraySize);

        return result;
    }

    /**
     * Return the adjacent open sites for a given site
     *
     * @param row
     * @param col
     * @return
     */
    private int[] getAdjacentOpenSites(int row, int col) {

        boolean topIsOpen = false;
        boolean bottomIsOpen = false;
        boolean leftIsOpen = false;
        boolean rightIsOpen = false;

        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;

        int countOpen = 0;

        // Get Top adjacent
        if ((row - 1) > 0) {
            if (isOpen(row - 1, col)) {
                top = getOneDimensionIndex(row - 1, col);
                topIsOpen = true;
                countOpen++;
            }
        } else if (row - 1 == 0) {
            top = this.n * this.n;
            topIsOpen = true;
            countOpen++;
        }

        // Get Bottom adjacent
        if ((row + 1) <= this.n) {
            if (isOpen(row + 1, col)) {
                bottom = getOneDimensionIndex(row + 1, col);
                bottomIsOpen = true;
                countOpen++;
            }
        } else if ((row + 1) == (this.n + 1)) {
            bottom = (this.n * this.n) + 1;
            bottomIsOpen = true;
            countOpen++;
        }

        // Get Left adjacent
        if ((col - 1) > 0) {
            if (isOpen(row, col - 1)) {
                left = getOneDimensionIndex(row, col - 1);
                leftIsOpen = true;
                countOpen++;
            }
        }

        // Get Right adjacent
        if ((col + 1) <= this.n) {
            if (isOpen(row, col + 1)) {
                right = getOneDimensionIndex(row, col + 1);
                rightIsOpen = true;
                countOpen++;
            }
        }

        // Create array of open sites to connect to
        int[] toOpen = new int[countOpen];

        for (int i = 0; i < countOpen; i++) {
            if (topIsOpen) {
                toOpen[i] = top;
                topIsOpen = false;
            } else if (bottomIsOpen) {
                toOpen[i] = bottom;
                bottomIsOpen = false;
            } else if (leftIsOpen) {
                toOpen[i] = left;
                leftIsOpen = false;
            } else if (rightIsOpen) {
                toOpen[i] = right;
                rightIsOpen = false;
            }
        }

        return toOpen;
    }
}
