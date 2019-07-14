import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;


public class SeamCarver {
  private Picture picture;
  private double[][] energy;
  private double[] energyTo;
  private int[] edgeTo;

  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException();
    }
    this.picture = new Picture(picture);
    energy = new double[picture.width()][picture.height()];
    for (int x = 0; x < width(); x++) {
      for (int y = 0; y < height(); y++) {
        energy(x, y);
      }
    }
  }

  public Picture picture() {
    return new Picture(picture);
  }                       // current picture

  private int getNumberOfVertices() {
    return picture.width() * picture.height();
  }
  public int width() {
    return picture.width();
  }                         // width of current picture

  public int height() {
    return picture.height();
  }                           // height of current picture

  private double deltaXSquare(int x, int y) {
    int right = picture.getRGB(x + 1, y);
    int left = picture.getRGB(x - 1, y);
    return Math.pow(((right >> 16) & 0xFF) - ((left >> 16) & 0xFF), 2)
        + Math.pow(((right >> 8) & 0xFF) - ((left >> 8) & 0xFF), 2)
        + Math.pow((right & 0xFF) - (left & 0xFF), 2);
  }

  private double deltaYSquare(int x, int y) {
    int top = picture.getRGB(x, y - 1);
    int down = picture.getRGB(x, y + 1);
    if (energy[x][y] != 0) {
      return energy[x][y];
    }
    return Math.pow(((down >> 16) & 0xFF) - ((top >> 16) & 0xFF), 2)
        + Math.pow(((down >> 8) & 0xFF) - ((top >> 8) & 0xFF), 2)
        + Math.pow((down & 0xFF) - (top & 0xFF), 2);
  }

  public double energy(int x, int y) {
    if (x < 0 || y < 0 || x >= width() || y >= height()) {
      throw new IllegalArgumentException();
    }
    if (x == 0 || y == 0 || x >= width() - 1 || y >= height() - 1) {
      return 1000;
    }
    if (energy[x][y] != 0.0) {
      return energy[x][y];
    }
    energy[x][y] = Math.sqrt(deltaXSquare(x, y) + deltaYSquare(x, y));
    return energy[x][y];
  }

  private int pixelNumber(int col, int row) {
    return width() * row + col;
  }

  private int colFromNumber(int pixelNumber) {
    return pixelNumber % width();
  }

  private int rowFromNumber(int pixelNumber) {
    return pixelNumber / width();
  }

  private ArrayList<Integer> getAdjacent(int v) {
    ArrayList<Integer> res = new ArrayList<>();
    int x = colFromNumber(v);
    int y = rowFromNumber(v);
    if (y >= height() - 1) {
      return res;
    }
    for (int i = x - 1; i < x + 2; i++) {
      if (i < 0 || i >= width()) {
        continue;
      }
      res.add(pixelNumber(i, y + 1));
    }
    return res;
  }

  public int[] findHorizontalSeam() {
    Picture original = picture;
    double[][] originalEnergy = energy;
    Picture transposed = new Picture(height(), width());
    double[][] transposedEnergy = new double[height()][width()];
    for (int x = 0; x < transposed.width(); x++) {
      for (int y = 0; y < transposed.height(); y++) {
        transposed.setRGB(x, y, picture.getRGB(y, x));
        transposedEnergy[x][y] = energy[y][x];
      }
    }
    picture = transposed;
    energy = transposedEnergy;
    int[] seam = findVerticalSeam();
    picture = original;
    energy = originalEnergy;
    return seam;
  }

  public int[] findVerticalSeam() {
    energyTo = new double[getNumberOfVertices()];
    edgeTo = new int[getNumberOfVertices()];
    for (int v = 0; v < getNumberOfVertices(); v++) {
      energyTo[v] = v < width() ? 1000 : Double.POSITIVE_INFINITY;
    }
    for (int v = 0; v < getNumberOfVertices(); v++) {
      for (int vv : getAdjacent(v)) {
        relax(v, vv);
      }
    }

    double minEnergy = Double.POSITIVE_INFINITY;
    int minIndex = 0;
    //get the minimal energy point in the last row
    for (int ii = getNumberOfVertices() - 1; ii > getNumberOfVertices() - width() - 1; ii--) {
      if (energyTo[ii] < minEnergy) {
        minEnergy = energyTo[ii];
        minIndex = ii;
      }
    }
    //reconstruct the path
    int y = height() - 1;
    int[] path = new int[height()];
    path[y] = colFromNumber(minIndex);
    while (y > 0) {
      y--;
      path[y] = colFromNumber(edgeTo[minIndex]);
      minIndex = edgeTo[minIndex];
    }
    return path;
  }

  private void relax(int v, int to) {
    if (energyTo[to] > energyTo[v] + energy(colFromNumber(to), rowFromNumber(to))) {
      energyTo[to] = energyTo[v] + energy(colFromNumber(to), rowFromNumber(to));
      edgeTo[to] = v;
    }
  }

  private void validateSeam(int[] seam) {
    if (seam == null) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < seam.length - 1; i++) {
      if (Math.abs(seam[i] - seam[i + 1]) > 1) {
        throw new IllegalArgumentException();
      }
    }
  }

  public void removeHorizontalSeam(int[] seam) {
    validateSeam(seam);
    if (seam.length != width()) {
      throw new IllegalArgumentException();
    }
    int x = 0;
    Picture carved = new Picture(picture.width(), picture.height() - 1);
    for (int seamY : seam) {
      if (seamY < 0 || seamY >= height()) {
        throw new IllegalArgumentException();
      }
      if (seamY > 0) {
        energy[x][seamY - 1] = 0;
      }
      energy[x][seamY] = 0;
      if (seamY < height() - 1) {
        energy[x][seamY + 1] = 0;
      }
      for (int i = 0; i < seamY; i++) {
        carved.setRGB(x, i, picture.getRGB(x, i));
      }
      for (int i = seamY; i < carved.height(); i++) {
        carved.setRGB(x, i, picture.getRGB(x, i + 1));
        energy[x][i] = energy[x][i+1];
      }
      x++;
    }
    this.picture = carved;
  }

  public void removeVerticalSeam(int[] seam) {
    validateSeam(seam);
    if (seam.length != height()) {
      throw new IllegalArgumentException();
    }
    int y = 0;
    Picture carved = new Picture(picture.width() - 1, picture.height());
    for (int seamX : seam) {
      if (seamX < 0 || seamX >= width()) {
        throw new IllegalArgumentException();
      }
      if (seamX > 0) {
        energy[seamX - 1][y] = 0;
      }
      energy[seamX][y] = 0;
      if (seamX < width() - 1) {
        energy[seamX + 1][y] = 0;
      }
      for (int i = 0; i < seamX; i++) {
        carved.setRGB(i, y, picture.getRGB(i, y));
      }
      for (int i = seamX; i < carved.width(); i++) {
        carved.setRGB(i, y, picture.getRGB(i + 1, y));
        energy[i][y] = energy[i + 1][y];
      }
      y++;
    }
    this.picture = carved;
  }     // remove vertical seam from current picture

  public static void main(String[] args) {
    Picture picture = new Picture(args[0]);
    SeamCarver sc = new SeamCarver(picture);
    for (int n : sc.findVerticalSeam()) {
      StdOut.print(n + "-");
    }
  }
}