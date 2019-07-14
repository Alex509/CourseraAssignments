import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Main {

  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    for (int i = 0; i < 10; i++) {
      BoggleBoard board = new BoggleBoard();
      solver.getAllValidWords(board);
      int score = 0;
      for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word + " " + solver.scoreOf(word));
        score += solver.scoreOf(word);
      }
      StdOut.println("Score = " + score);
    }
  }
}
