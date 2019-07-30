/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double MAGIC_PROBABILITY_NUMBER = 1.96;
    private final int size;
    private final double meanA;
    private final double stdDev;
    private final double coLo;
    private final double coHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] experimentResults = new double[trials];
        this.size = n;
        for (int i = 0; i < trials; i++) {
            experimentResults[i] = this.doExperiment();
        }
        this.meanA = StdStats.mean(experimentResults);
        this.stdDev = StdStats.stddev(experimentResults);
        this.coLo = this.meanA - MAGIC_PROBABILITY_NUMBER * this.stdDev / Math.sqrt(trials);
        this.coHi = this.meanA + MAGIC_PROBABILITY_NUMBER * this.stdDev / Math.sqrt(trials);
    }

    private double doExperiment() {
        Percolation percolation = new Percolation(this.size);
        while (true) {
            int randRow = StdRandom.uniform(1, this.size + 1);
            int randCol = StdRandom.uniform(1, this.size + 1);
            percolation.open(randRow, randCol);
            if (percolation.percolates()) {
                return (double) percolation.numberOfOpenSites() / (this.size * this.size);
            }
        }
    }

    public double mean() {
        return this.meanA;
    }

    public double stddev() {
        return this.stdDev;
    }

    public double confidenceLo() {
        return this.coLo;
    }

    public double confidenceHi() {
        return this.coHi;
    }

    public static void main(String[] args) {
        PercolationStats perc = new PercolationStats(Integer.parseInt(args[0]),
                                                     Integer.parseInt(args[1]));
        System.out.println(perc.mean());
        System.out.println(perc.stddev());
        System.out.println(perc.confidenceLo());
        System.out.println(perc.confidenceHi());
    }
}
