import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Solver {
    private Board[] solutionSequence;
    private final short numberOfMoves;
    private final boolean solvable;

    private class SearchNode {
        private final Board board;
        private final byte step;
        private final int manhattan;
        private final short hamming;
        private final int priorityP;
        private final SearchNode predecessor;

        SearchNode(Board board, int step, SearchNode predecessor) {
            this.board = board;
            this.step = (byte) step;
            this.predecessor = predecessor;
            this.manhattan = this.board.manhattan();
            this.hamming = (short) this.board.hamming();
            this.priorityP = this.manhattan + this.step;
        }

        public Board getBoard() {
            return this.board;
        }

        public int priority() {
            return this.priorityP;
        }

        public int getStep() {
            return this.step;
        }

        public short getHamming() {
            return this.hamming;
        }

        public int getManhattan() {
            return this.manhattan;
        }

        public SearchNode previous() {
            return this.predecessor;
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        MinPQ<SearchNode> boardPQ = new MinPQ<>(this.comparator());
        MinPQ<SearchNode> twinyPQ = new MinPQ<>(this.comparator());

        boardPQ.insert(new SearchNode(initial, 0, null));
        twinyPQ.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode currentNode;
        Board previousBoard;

        while (true) {
            currentNode = boardPQ.delMin();
            if (currentNode.getBoard().isGoal()) {
                this.solvable = true;
                this.numberOfMoves = (short) currentNode.getStep();
                this.solutionSequence = new Board[this.numberOfMoves + 1];
                for (short i = this.numberOfMoves; i >= 0; i--) {
                    this.solutionSequence[i] = currentNode.getBoard();
                    currentNode = currentNode.previous();
                }
                return;
            }
            if (currentNode.previous() != null) {
                previousBoard = currentNode.previous().getBoard();
            }
            else {
                previousBoard = null;
            }
            for (Board neigbour : currentNode.board.neighbors()) {
                if (!neigbour.equals(previousBoard)) {
                    boardPQ.insert(
                            new SearchNode(neigbour, currentNode.getStep() + 1, currentNode));
                }
            }

            currentNode = twinyPQ.delMin();
            if (currentNode.getBoard().isGoal()) {
                this.solvable = false;
                this.numberOfMoves = -1;
                this.solutionSequence = null;
                return;
            }
            if (currentNode.previous() != null) {
                previousBoard = currentNode.previous().getBoard();
            }
            else {
                previousBoard = null;
            }
            for (Board neigbour : currentNode.board.neighbors()) {
                if (!neigbour.equals(previousBoard)) {
                    twinyPQ.insert(
                            new SearchNode(neigbour, currentNode.getStep() + 1, currentNode));
                }
            }
        }
    }

    private class MyComparator implements Comparator<SearchNode> {
        public int compare(SearchNode s1, SearchNode s2) {
            int comp = s1.priority() - s2.priority();
            if (comp != 0) {
                return comp;
            }
            comp = s1.getManhattan() - s2.getManhattan();
            if (comp != 0) {
                return comp;
            }
            comp = s1.getHamming() - s2.getHamming();
            return comp;
        }

    }

    private Comparator<SearchNode> comparator() {
        return new MyComparator();
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public int moves() {
        return this.numberOfMoves;
    }

    public Iterable<Board> solution() {
        if (solutionSequence != null) {
            return Arrays.asList(solutionSequence);
        }
        return null;
    }

    public static void main(String[] args) {
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
        StdOut.println("Solving " + args[0]);
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
