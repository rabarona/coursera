import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo Barona on 3/18/17.
 *
 * Board representation for 8 puzzle.
 */
public class Board {

    private final char[] blocks;

    public Board(int[][] blocks) {

        this.blocks = new char[blocks.length * blocks.length];
        int dimension = getDimension();

        int counter = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {

                this.blocks[counter] = (char) blocks[i][j];
                counter++;
            }
        }
    }

    public static void main(String[] args) {

        int[][] blocks = {{0, 4, 1}, {5, 3, 2}, {7, 8, 6}};
        Board b = new Board(blocks);
        StdOut.println("Is goal? " + b.isGoal());
        StdOut.println("Initial Board");
        StdOut.println(b.toString());
        StdOut.println("Hamming: " + b.hamming());
        StdOut.println("Manhattan: " + b.manhattan());
        Board twin = b.twin();
        StdOut.println("The twin");
        StdOut.println(twin.toString());
        StdOut.println("---- The neighbors ----");
        b.neighbors().forEach(board -> {
            StdOut.println(">>>><<<<<");
            StdOut.println(board.toString());
            StdOut.println("Hamming: " + board.hamming());
            StdOut.println("Manhattan: " + board.manhattan());
        });

    }

    // board dimension n
    public int dimension() {
        return getDimension();
    }

    // number of blocks out of place
    public int hamming() {

        int hamming = numbersOutOfPlace();

        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {

        int manhattan = 0;
        int y = 0;
        int dimension = getDimension();
        int size = this.blocks.length;

        int[] ys = new int[size];
        for (int i = 0; i < size; i++) {
            y++;
            ys[i] = y;

            if (y == dimension) y = 0;
        }

        y = 0;

        for (int i = 0; i < size; i++) {

            y++;
            int value = this.blocks[i];

            if (value != 0) {
                // where it should be?
                int eX = ((value - 1) / dimension) + 1;
                int eY = ys[value - 1];

                // where is it?
                int cX = (i / dimension) + 1;
                int cY = y;

                // how many moves?
                int currentMovesX = (cX - eX);
                if (currentMovesX < 0) currentMovesX = currentMovesX * -1;

                int currentMovesY = (cY - eY);
                if (currentMovesY < 0) currentMovesY = currentMovesY * -1;

                int currentMoves = currentMovesX + currentMovesY;

                manhattan += currentMoves;
            }

            if (y == dimension) y = 0;
        }

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {

        if (numbersOutOfPlace() == 0) return true;
        return false;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        int size = this.blocks.length;

        int indexA = StdRandom.uniform(0, size);
        int indexB = StdRandom.uniform(0, size);

        while (this.blocks[indexA] == 0 || this.blocks[indexB] == 0 ||
                indexA == indexB) {
            indexA = StdRandom.uniform(0, size);
            indexB = StdRandom.uniform(0, size);
        }

        char[] oneDimensionNewBlocks = switchPlaces(this.blocks, indexA, indexB);

        int[][] newBlocks = oneToTwoDimensions(oneDimensionNewBlocks);

        return new Board(newBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        int size = this.blocks.length;

        Board that = (Board) y;

        if (this.dimension() != that.dimension()) return false;

        for (int i = 0; i < size; i++) {
            if (this.blocks[i] != that.blocks[i]) return false;
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        List<Board> neighbors = new ArrayList<>();
        int size = this.blocks.length;
        int dimension = getDimension();

        // Find 0
        int indexOfBlank = -1;
        for (int i = 0; i < size; i++) {
            if (this.blocks[i] == 0) {
                indexOfBlank = i;
                break;
            }
        }

        // Current location in (x,y) format
        int blankX = (indexOfBlank / dimension) + 1;
        int blankY = (indexOfBlank - ((indexOfBlank / dimension)
                * dimension)) + 1;

        // check if it can move up
        if ((blankX - 1) > 0) {
            int indexOfSwitch = ((((blankX - 1) - 1) * dimension) + blankY) - 1;

            int[][] newBlocks = oneToTwoDimensions(switchPlaces(this.blocks,
                    indexOfBlank, indexOfSwitch));

            Board newBoard = new Board(newBlocks);
            neighbors.add(newBoard);
        }

        // check if it can move down
        if ((blankX + 1) <= dimension) {
            int indexOfSwitch = ((((blankX + 1) - 1) * dimension) + blankY) - 1;

            int[][] newBlocks = oneToTwoDimensions(switchPlaces(this.blocks,
                    indexOfBlank, indexOfSwitch));

            Board newBoard = new Board(newBlocks);
            neighbors.add(newBoard);
        }

        // check if it can move left
        if ((blankY - 1) > 0) {
            int indexOfSwitch = (((blankX - 1) * dimension) + (blankY - 1)) - 1;

            int[][] newBlocks = oneToTwoDimensions(switchPlaces(this.blocks,
                    indexOfBlank, indexOfSwitch));

            Board newBoard = new Board(newBlocks);
            neighbors.add(newBoard);
        }

        // check if it can move right
        if ((blankY + 1) <= dimension) {
            int indexOfSwitch = (((blankX - 1) * dimension) + (blankY + 1) - 1);

            int[][] newBlocks = oneToTwoDimensions(switchPlaces(this.blocks,
                    indexOfBlank, indexOfSwitch));

            Board newBoard = new Board(newBlocks);
            neighbors.add(newBoard);
        }

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {

        int dimension = getDimension();
        StringBuilder stringBoard = new StringBuilder();
        int y = 0;

        stringBoard.append(dimension + "\n");
        for (int i = 0; i < this.blocks.length; i++) {

            y++;

            stringBoard.append(String.format("%2d ", (int) this.blocks[i]));
            if (y == dimension && !(i == this.blocks.length - 1)) {
                y = 0;
                stringBoard.append("\n");
            }
        }

        return stringBoard.toString();
    }

    private char[] switchPlaces(char[] arr, int indexA, int indexB) {

        int size = arr.length;

        int valueA = arr[indexA];
        int valueB = arr[indexB];

        char[] newBlocks = new char[size];

        for (int i = 0; i < newBlocks.length; i++) {
            if (i == indexA) {
                newBlocks[i] = (char) valueB;
            } else if (i == indexB) {
                newBlocks[i] = (char) valueA;
            } else {
                newBlocks[i] = arr[i];
            }
        }

        return newBlocks;
    }

    private int[][] oneToTwoDimensions(char[] arr) {

        int dimension = (int) Math.sqrt(arr.length);
        int[][] anyBlocks = new int[dimension][dimension];


        int counter = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                anyBlocks[i][j] = arr[counter];
                counter++;
            }
        }

        return anyBlocks;
    }

    private int numbersOutOfPlace() {

        int outOfPlace = 0;
        int size = this.blocks.length;

        for (int i = 0; i < size; i++) {
            if (i != ((int) this.blocks[i] - 1)) {
                if ((int) this.blocks[i] != 0) {
                    outOfPlace++;
                }
            }
        }

        return outOfPlace;
    }

    private int getDimension() {
        return (int) Math.sqrt(this.blocks.length);
    }
}
