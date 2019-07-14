import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF weightedQuickUnionUF;
  private boolean[] opened;
  private int countOfOpen;
  private final int tuppleSize;
  private int topVertex;
  private int sinkVertex;

  // create n-by-n grid, with all sites blocked
  public Percolation(final int size) {
    weightedQuickUnionUF = new WeightedQuickUnionUF(size * size + 2);
    topVertex = size * size;
    sinkVertex = size * size + 1;
    tuppleSize = size;
    opened = new boolean[size * size];
    countOfOpen = 0;

    //connect tow virtual nodes to the network
    for (int i = 0; i < tuppleSize; i++) {
      weightedQuickUnionUF.union(topVertex, i);
      weightedQuickUnionUF.union(sinkVertex, (tuppleSize * tuppleSize - 1 - i));
    }
  }

  public void open(int row, int col) {
    checkBounds(row, col);
    if (isOpen(row, col)) {
      return;
    }
    opened[rowColToInt(row, col)] = true;
    countOfOpen++;
    if (row > 1 && isOpen(row - 1, col)) {
      weightedQuickUnionUF.union(rowColToInt(row, col), rowColToInt(row - 1, col));
    }
    if (row < tuppleSize && isOpen(row + 1, col)) {
      weightedQuickUnionUF.union(rowColToInt(row, col), rowColToInt(row + 1, col));
    }
    if (col < tuppleSize && isOpen(row, col + 1)) {
      weightedQuickUnionUF.union(rowColToInt(row, col), rowColToInt(row, col + 1));
    }
    if (col > 1 && isOpen(row, col - 1)) {
      weightedQuickUnionUF.union(rowColToInt(row, col), rowColToInt(row, col - 1));
    }
  }   // open site (row, col) if it is not open already


 //  is site (row, col) open?
  public boolean isOpen(int row, int col) {
    checkBounds(row, col);
    return opened[rowColToInt(row, col)];
  }

  private void checkBounds(int row, int col) {
    if (row <= 0 || row > tuppleSize) {
      throw new IllegalArgumentException("row index i out of bounds");
    }
    if (col <= 0 || col > tuppleSize) {
      throw new IllegalArgumentException("col index i out of bounds");
    }
  }

  private int rowColToInt(int row, int col) {
    checkBounds(row, col);
    return (((row - 1) * tuppleSize) + (col - 1));
  }

  public boolean isFull(int row, int col) {
    checkBounds(row, col);
    return isOpen(row, col) && weightedQuickUnionUF.connected(topVertex, rowColToInt(row, col));
  }


  public int numberOfOpenSites() {
    return countOfOpen;
  }

  // does the system percolate?
  public boolean percolates() {
    return weightedQuickUnionUF.connected(topVertex, sinkVertex);
  }

  public static void main(String[] args) {
    In input = new In(args[0]);
    int size = Integer.parseInt(input.readLine());
    Percolation percolation = new Percolation(size);
    String[] lines = input.readAllLines();
    for (String line: lines) {
      percolation.open(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
    }
  }  // test client (optional)
}