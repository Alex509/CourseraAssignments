import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private int[] results;
  private int size;
  private int trials;
  private double mean;

  // perform trials independent experiments on an n-by-n grid
  public PercolationStats(int n, int trials) {
    results = new int[trials];
    size = n * n;
    this.trials = trials;
    for (int i = 0; i < trials; i++) {
      results[i] = experiment(n);
    }
    mean = StdStats.mean(results)/size;
  }

  private int experiment(int n) {
    Percolation percolation = new Percolation(n);

    while(!percolation.percolates()) {
      percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
    }
    return percolation.numberOfOpenSites();
  }

//  // sample mean of percolation threshold
  public double mean() {
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(results)/size;
  }

  // low  endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean - (1.96 * stddev() / Math.sqrt(trials));
  }

  public double confidenceHi() {
    return mean + (1.96 * stddev() / Math.sqrt(trials));
  }

  // test client (described below)
  public static void main(String[] args) {

  }
}