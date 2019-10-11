import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
 * Makes set of Monte-Carlo simulations. The aim is to understand
 * what ratio of emptyness volume to all solid body volume is sufficient
 * to percolation.
 *
 * For calculations two-sided Student's t-distribution is used. We choose
 * 95% confidence as enough for us.
 */

public class PercolationStats {
    // for simplicity we assume, that we do enormous amount of
    // trials (>30). That allow us to use coeff for infinity
    // number of trials
    private static final double STUDENTS_COEFF = 1.96;
    private final double cachedMean;
    private final double cachedStandardDerivation;
    private final double lowBoundaryOfStudentsInterval;
    private final double highBoundaryOfStudentsInterval;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] experimentResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            experimentResults[i] = this.doExperiment(n);
        }

        cachedMean = StdStats.mean(experimentResults);
        cachedStandardDerivation = StdStats.stddev(experimentResults);
        lowBoundaryOfStudentsInterval = cachedMean - STUDENTS_COEFF * cachedStandardDerivation / Math.sqrt(trials);
        highBoundaryOfStudentsInterval = cachedMean + STUDENTS_COEFF * cachedStandardDerivation / Math.sqrt(trials);
    }

    private double doExperiment(int size) {
        Percolation percolation = new Percolation(size);
        while (true) {
            int randRow = StdRandom.uniform(1, size + 1);
            int randCol = StdRandom.uniform(1, size + 1);
            percolation.open(randRow, randCol);
            if (percolation.percolates()) {
                return (double) percolation.numberOfOpenSites() / (size * size);
            }
        }
    }

    public double mean() {
        return this.cachedMean;
    }

    public double stddev() {
        return this.cachedStandardDerivation;
    }

    public double confidenceLo() {
        return this.lowBoundaryOfStudentsInterval;
    }

    public double confidenceHi() {
        return this.highBoundaryOfStudentsInterval;
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
