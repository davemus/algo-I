/* *****************************************************************************
 *  Name: Percolation. Second Try
 *  Date: 1st of December 2018
 *  Description: Missing
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private static final int TOP_SITE_NUMBER = 0;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final int side;
    private boolean[][] openSites;
    private final int botSiteNumber;
    private int openSitesQuantity;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        int sites = n * n + 2; // 2 sites for virtual top and bot
        this.side = n;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(sites);
        this.openSites = new boolean[n][n];
        this.botSiteNumber = sites - 1;
        this.openSitesQuantity = 0;
    }

    private int toWeightUnionNum(int row, int col) {
        return (row - 1) * this.side + col;
    }

    private boolean isValidRowColNum(int tested) {
        return 1 <= tested && tested <= this.side;
    }

    private void connectIfNeighbourIfValidAndOpen(int row, int col, int
            neighbourRow, int neighbourCol) {
        if (this.isValidRowColNum(neighbourRow) && this.isValidRowColNum(neighbourCol)) {
            if (this.openSites[neighbourRow - 1][neighbourCol - 1]) {
                this.weightedQuickUnionUF.union(
                        this.toWeightUnionNum(row, col),
                        this.toWeightUnionNum(neighbourRow, neighbourCol)
                );
            }
        }
    }

    private void connectNeighboursIfOpen(int row, int col) {
        if (row == 1) {
            this.weightedQuickUnionUF.union(TOP_SITE_NUMBER, this.toWeightUnionNum(row, col));
        }
        this.connectIfNeighbourIfValidAndOpen(row, col, row + 1, col);
        this.connectIfNeighbourIfValidAndOpen(row, col, row - 1, col);
        this.connectIfNeighbourIfValidAndOpen(row, col, row, col + 1);
        this.connectIfNeighbourIfValidAndOpen(row, col, row, col - 1);
        if (row == this.side) {
            this.weightedQuickUnionUF.union(this.botSiteNumber, this.toWeightUnionNum(row, col));
        }
    }

    public void open(int row, int col) {
        if (!this.isValidRowColNum(row) || !this.isValidRowColNum(col)) {
            throw new IllegalArgumentException();
        }
        if (!this.isOpen(row, col)) {
            this.openSites[row - 1][col - 1] = true;
            this.connectNeighboursIfOpen(row, col);
            this.openSitesQuantity += 1;
        }
    }

    public boolean isOpen(int row, int col) {
        if (!this.isValidRowColNum(row) || !this.isValidRowColNum(col)) {
            throw new IllegalArgumentException();
        }
        return this.openSites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (!this.isValidRowColNum(row) || !this.isValidRowColNum(col)) {
            throw new IllegalArgumentException();
        }
        return this.openSites[row - 1][col - 1] &&
                this.weightedQuickUnionUF
                        .connected(TOP_SITE_NUMBER, this.toWeightUnionNum(row, col));
    }

    public int numberOfOpenSites() {
        return this.openSitesQuantity;
    }

    public boolean percolates() {
        return this.weightedQuickUnionUF.connected(TOP_SITE_NUMBER, this.botSiteNumber);
    }

    public static void main(String[] args) {
	Percolation percolation = new Percolation(3);
	int[][] pointsToOpen = {{1, 1}, {2, 1}, {3, 1}};
	for (int[] point: pointsToOpen) {
		assert !percolation.percolates();
		percolation.open(point[0], point[1]);
	}
	assert percolation.percolates();
    }
}
