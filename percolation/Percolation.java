import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Two-dimensional percolation model. Simulates passing liquid or gas
 * (futher filler) through porous material (futher filter) from top to
 * bottom.
 *
 * Filter is modelled as 2d square grid, consisting of cells.
 * The filler could pass between cells if they are neigbours and both
 * are open. The neighbours for cell are given as cells next to it by
 * horizontal or vertical. 
 **/

public class Percolation {
	private static final int TOP_SITE_NUMBER = 0;
	private final int botSiteNumber;
	private final WeightedQuickUnionUF weightedQuickUnionUF;
	private final int side;
	private boolean[][] openSites;
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

	private class Cell {
		public final int col;
		public final int row;

		Cell(int row, int col) {
			this.col = col;
			this.row = row;
		}

		Cell[] neighbours() {
			Cell[] cells = {
				new Cell(row + 1, col),
				new Cell(row - 1, col),
				new Cell(row, col + 1),
				new Cell(row, col - 1),
			};
			return cells;
		}

		private int toWeightUnionNum() {
			return (row - 1) * side + col;
		}

		private boolean isValid() {
			return isValidRowColNum(row) && isValidRowColNum(col);
		}

		private void open() {
			if (isOpen()) {
				throw new IllegalArgumentException();
			}
			openSites[row - 1][col - 1] = true;
			connectNeighboursIfOpen();
			openSitesQuantity += 1;
		}

		private boolean isOpen() {
			return openSites[row - 1][col - 1];
		}

		private void connectNeighboursIfOpen() {
			if (row == 1) {
				weightedQuickUnionUF.union(TOP_SITE_NUMBER, toWeightUnionNum());
			}
			for (Cell neighbour: neighbours()) {
				connectIfNeighbourExistsAndIsOpen(neighbour);
			}
			if (row == side) {
				weightedQuickUnionUF.union(botSiteNumber, toWeightUnionNum());
			}
		}

		private void connectIfNeighbourExistsAndIsOpen(Cell neighbour) {
			if (neighbour.isValid() && neighbour.isOpen()) {
				weightedQuickUnionUF.union(
				toWeightUnionNum(), neighbour.toWeightUnionNum());
			}
		}

		private boolean isConnectedToTop() {
			return weightedQuickUnionUF.connected(TOP_SITE_NUMBER, toWeightUnionNum());
		}
	}

	private boolean isValidRowColNum(int tested) {
		return 1 <= tested && tested <= this.side;
	}

	public void open(int row, int col) {
		Cell cell = new Cell(row, col);
		if (!cell.isValid()) {
			throw new IllegalArgumentException();
		}
		if (!cell.isOpen()) {
			cell.open();
		}
	}

	public boolean isOpen(int row, int col) {
		Cell cell = new Cell(row, col);
		if (!cell.isValid()) {
			throw new IllegalArgumentException();
		}
		return cell.isOpen();
	}

	public boolean isFull(int row, int col) {
		Cell cell = new Cell(row, col);
		if (!cell.isValid()) {
			throw new IllegalArgumentException();
		}
		return cell.isOpen() && cell.isConnectedToTop();
	}

	public int numberOfOpenSites() {
		return openSitesQuantity;
	}

	public boolean percolates() {
		return weightedQuickUnionUF.connected(TOP_SITE_NUMBER, botSiteNumber);
	}

	public static void main(String[] args) {
		Percolation percolation = new Percolation(3);
		int[][] pointsToOpen = {
			{1,1},
			{2,1},
			{3,1}
		};
		for (int[] point: pointsToOpen) {
			assert ! percolation.percolates();
			percolation.open(point[0], point[1]);
		}
		assert percolation.percolates();
	}
}
