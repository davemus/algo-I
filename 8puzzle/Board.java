import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {
    private final short[][] state;
    private final short hammingP;
    private final int manhattanP;
    private byte zerox;
    private byte zeroy;
    private final byte dimensionP;

    public Board(int[][] blocks) {
        this.state = new short[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.state[i][j] = (short) blocks[i][j];
            }
        }

        int expectedI;
        int expectedJ;

        this.dimensionP = (byte) this.state.length;

        int ham = 0, man = 0;
        for (int i = 0; i < this.dimensionP; i++) {
            for (int j = 0; j < this.dimensionP; j++) {
                if (blocks[i][j] == 0) {
                    this.zerox = (byte) i;
                    this.zeroy = (byte) j;
                }
                else if (blocks[i][j] != this.dimensionP * i + j + 1) {
                    ham++;
                    expectedI = (blocks[i][j] - 1) / this.dimensionP;
                    expectedJ = (blocks[i][j] - 1) % this.dimensionP;
                    man += (Math.abs(expectedI - i) + Math.abs(expectedJ - j));
                }
            }
        }

        this.hammingP = (short) ham;
        this.manhattanP = man;
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return this.dimensionP;
    }

    public int hamming() {
        return this.hammingP;
    }

    public int manhattan() {
        return this.manhattanP;
    }

    public boolean isGoal() {
        return this.manhattan() == 0;
    }

    private void exchange(int ix, int iy, int jx, int jy) {
        // Make deep copy!
        short exchanger = this.state[ix][iy];
        this.state[ix][iy] = this.state[jx][jy];
        this.state[jx][jy] = exchanger;
    }

    public Board twin() {
        int x1, x2, y1, y2;
        if (this.zerox != 0 || this.zeroy != 0) {
            x1 = 0;
            y1 = 0;
            if (this.zerox != 0 || this.zeroy != 1) {
                x2 = 0;
                y2 = 1;
            }
            else {
                x2 = 1;
                y2 = 0;
            }
        }
        else {
            x1 = 0;
            y1 = 1;
            x2 = 1;
            y2 = 1;
        }
        return this.createChanged(x1, y1, x2, y2);
    }

    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        else {
            Board b = (Board) y;
            if (b.state.length != this.dimensionP || b.state[0].length != this.dimensionP) {
                return false;
            }
            for (int i = 0; i < this.dimensionP; i++) {
                for (int j = 0; j < this.dimensionP; j++) {
                    if (this.state[i][j] != b.state[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Board createChanged(int x1, int y1, int x2, int y2) {
        this.exchange(x1, y1, x2, y2);
        int[][] changedState = new int[this.dimensionP][this.dimensionP];
        for (int i = 0; i < this.dimensionP; i++) {
            for (int j = 0; j < this.dimensionP; j++) {
                changedState[i][j] = this.state[i][j];
            }
        }
        Board changed = new Board(changedState);
        this.exchange(x1, y1, x2, y2);
        return changed;
    }

    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbours = new LinkedList<>();
        if (this.zerox != 0) {
            neighbours.add(this.createChanged(this.zerox, this.zeroy, this.zerox - 1, this.zeroy));
        }
        if (this.zerox != this.dimensionP - 1) {
            neighbours.add(this.createChanged(this.zerox, this.zeroy, this.zerox + 1, this.zeroy));
        }
        if (this.zeroy != 0) {
            neighbours.add(this.createChanged(this.zerox, this.zeroy, this.zerox, this.zeroy - 1));
        }
        if (this.zeroy != this.dimensionP - 1) {
            neighbours.add(this.createChanged(this.zerox, this.zeroy, this.zerox, this.zeroy + 1));
        }
        return neighbours;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimensionP);
        sb.append("\n");
        for (int i = 0; i < this.dimensionP; i++) {
            for (int j = 0; j < this.dimensionP; j++) {
                sb.append(" ");
                sb.append(this.state[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board board = new Board(tiles);
        StdOut.println(board);
        StdOut.println(board.twin());
        for (Board neigh : board.neighbors()) {
            StdOut.println(neigh);
        }
    }
}
