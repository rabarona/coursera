import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Ricardo Barona on 3/18/17.
 *
 * Dependencies algs4.jar
 *
 * Solver for 8 puzzle using A* algorithm.
 */
public class Solver {

    private boolean isSolvable;
    private Deque<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Initial board " +
                "can't be null, please use a valid initial board");

        this.isSolvable = true;
        this.solution = new ArrayDeque<>();

        final MinPQ<SearchNode> minPQ = new MinPQ<>();
        final MinPQ<SearchNode> twinMinPQ = new MinPQ<>();
        SearchNode solutionSearchNode = null;

        // First, insert the initial search node into the priority queue
        SearchNode initialSearchNode = new SearchNode(initial, null, 0,
                initial.manhattan());
        minPQ.insert(initialSearchNode);

        // Then insert the initial search node into the unsolvable PQ
        Board initialBoardTwin = initial.twin();
        SearchNode initialTwinSearchNode = new SearchNode(initialBoardTwin,
                null, 0, initialBoardTwin.manhattan());
        twinMinPQ.insert(initialTwinSearchNode);

        while (!minPQ.isEmpty() && !twinMinPQ.isEmpty()) {

            SearchNode potentialGoalBoard = minPQ.delMin();
            SearchNode potentialGoalTwinBoard = twinMinPQ.delMin();

            if (potentialGoalTwinBoard.getBoard().isGoal()) {
                this.isSolvable = false;
                break;
            }

            if (potentialGoalBoard.getBoard().isGoal()) {
                solutionSearchNode = potentialGoalBoard;
                break;
            }

            potentialGoalBoard.getBoard().neighbors()
                    .forEach((Board neighbor) -> {
                        if (potentialGoalBoard.getPrev() == null ||
                                !neighbor.equals(potentialGoalBoard
                                        .getPrev().getBoard())) {
                            minPQ.insert(new SearchNode(neighbor,
                                    potentialGoalBoard,
                                    potentialGoalBoard.getMoves() + 1,
                                    neighbor.manhattan()));
                        }
                    });

            potentialGoalTwinBoard.getBoard().neighbors()
                    .forEach((Board neighbor) -> {
                        if (potentialGoalTwinBoard.getPrev() == null ||
                                !neighbor.equals(potentialGoalTwinBoard
                                        .getPrev().getBoard())) {
                            twinMinPQ.insert(new SearchNode(neighbor,
                                    potentialGoalTwinBoard,
                                    potentialGoalTwinBoard.getMoves() + 1,
                                    neighbor.manhattan()));
                        }
                    });

        }

        if (solutionSearchNode != null && this.isSolvable) {

            solution.push(solutionSearchNode.getBoard());
            SearchNode prev = solutionSearchNode.getPrev();
            if (prev != null) {

                solution.push(prev.getBoard());

                while (prev.getPrev() != null) {
                    prev = prev.getPrev();
                    solution.push(prev.getBoard());
                }
            }

        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.solution.size() == 0) return null;

        return this.solution;
    }

    private static class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final SearchNode prev;
        private final int moves;
        private final int priority;

        SearchNode(Board board, SearchNode prev, int moves, int function) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.priority = function + moves;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public int getPriority() {
            return priority;
        }

        public int getMoves() { return moves; }

        @Override
        public int compareTo(SearchNode that) {

            if (that == null) throw new NullPointerException("CompareTo can't" +
                    " be null");

            if (!this.getClass().equals(that.getClass()))
                throw new ClassCastException("The object is not a SearchNode");

            if (this.priority == that.priority
                    && this.moves == that.moves) return 0;

            if (this.priority == that.priority
                    && this.moves < that.moves) return 1;

            if (this.priority > that.priority) return 1;

            return -1;
        }
    }

}
