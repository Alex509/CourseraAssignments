import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleSolver {
  private static final char Q_LITERAL = 'Q';
  private static final char U_LITERAL = 'U';
  private final FastTST<Integer> dictionary;
  private final Set<String> words;
  private boolean[][] marked;
  private int rows;
  private int columns;
  private final int[] points = {0, 0, 0, 1, 1, 2, 3, 5, 11};

  // Initializes the data st
  // ructure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(final String[] dictionary) {
    words = new HashSet<>();
    this.dictionary = new FastTST<>();
    for (String st:dictionary) {
      if (st.length() < 3) {
        continue;
      }
      int wordScore = (st.length() >= points.length - 1)
          ? points[points.length - 1]
          : points[st.length()];
      this.dictionary.put(st, wordScore);
    }
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(final BoggleBoard board) {
    rows = board.rows();
    columns = board.cols();
    marked = new boolean[rows][columns];
    words.clear();

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        int[] v = {row, column};
        dfs(board, v, new ArrayList<>());
      }
    }
    return words;
  }

  private void validateVertex(int[] vertex) {
    if (vertex[0] < 0 || vertex[0] >= rows && vertex[1] < 0 || vertex[1] >= columns) {
      throw new IllegalArgumentException("Vertex Out Of bonds");
    }
  }

  private boolean isMarked(int[] vertex) {
    validateVertex(vertex);
    return marked[vertex[0]][vertex[1]];
  }

  private void mark(int[] vertex) {
    validateVertex(vertex);
    marked[vertex[0]][vertex[1]] = true;
  }

  private void unmark(int[] vertex) {
    validateVertex(vertex);
    marked[vertex[0]][vertex[1]] = false;
  }

  private Iterable<int[]> getAdjacent(int[] vertex) {
    validateVertex(vertex);
    ArrayList<int[]> adjacent = new ArrayList<>();
    if (vertex[1] > 0) {
      if (vertex[0] > 0) {
        int[] topleft = {vertex[0] - 1, vertex[1] - 1};
        adjacent.add(topleft);
      }
      int[] left = {vertex[0], vertex[1] - 1};
      adjacent.add(left);
      if (vertex[0] < rows - 1) {
        int[] leftbottom = {vertex[0] + 1, vertex[1] - 1};
        adjacent.add(leftbottom);
      }
    }
    if (vertex[0] > 0) {
      int[] top = {vertex[0] - 1, vertex[1]};
      adjacent.add(top);
    }
    if (vertex[0] < rows - 1) {
      int[] bottom = {vertex[0] + 1, vertex[1]};
      adjacent.add(bottom);
    }
    if (vertex[1] < columns - 1) {
      int[] right = {vertex[0], vertex[1] + 1};
      adjacent.add(right);
      if (vertex[0] > 0) {
        int[] topright = {vertex[0] - 1, vertex[1] + 1};
        adjacent.add(topright);
      }
      if (vertex[0] < rows - 1) {
        int[] bottomright = {vertex[0] + 1, vertex[1] + 1};
        adjacent.add(bottomright);
      }
    }
    return adjacent;
  }

  private String getPathString(BoggleBoard board, Iterable<int[]> path) {
    StringBuilder result = new StringBuilder();
    for (int[] v : path) {
      char letter = board.getLetter(v[0], v[1]);
      result.append(letter);
      if (letter == Q_LITERAL) {
        result.append(U_LITERAL);
      }
    }
    return result.toString();
  }

  private void dfs(BoggleBoard board, final int[] vertex, List<int[]> path) {
    if (path.contains(vertex)) {
      return;
    }
    mark(vertex);
    path.add(vertex);

    String pathString = getPathString(board, path);
    if (path.size() > 1  && !dictionary.hasPrefix(pathString)) {
      path.remove(vertex);
      unmark(vertex);
      return;
    }
    if (dictionary.contains(pathString) && pathString.length() > 2) {
      words.add(pathString);
    }

    for (int[] v : getAdjacent(vertex)) {
      if (!isMarked(v)) {
        dfs(board, v, (path));
      }
    }
    unmark(vertex);
    path.remove(vertex);
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    Integer result = this.dictionary.get(word);
    return result == null ? 0 : result;
  }
}