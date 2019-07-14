import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
  private BreadthFirstDirectedPaths g1, g2;
  private final Digraph graph;
  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) {
      throw new IllegalArgumentException("Wrong Constructor argument");
    }
    graph = new Digraph(G);
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    g1 = new BreadthFirstDirectedPaths(graph, v);
    g2 = new BreadthFirstDirectedPaths(graph, w);
    int vertex = graph.V();
    int minLength = Integer.MAX_VALUE;
    for (int i = 0; i < vertex; i++) {
      if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
        minLength = Math.min(minLength,
          g1.distTo(i) + g2.distTo(i));
      }
    }
    if (minLength == Integer.MAX_VALUE) return -1;
    return minLength;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    g1 = new BreadthFirstDirectedPaths(graph, v);
    g2 = new BreadthFirstDirectedPaths(graph, w);
    int vertex = graph.V();
    int minLength = Integer.MAX_VALUE;
    int result = -1;
    for (int i = 0; i < vertex; i++) {
      if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
        int cur = g1.distTo(i) + g2.distTo(i);
        if (cur < minLength) {
          minLength = cur;
          result = i;
        }
      }
    }
    if (minLength == Integer.MAX_VALUE) return -1;
    return result;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("Illegal arguments");
    }
    int minLength = Integer.MAX_VALUE;
    for (int i: v) {
      for (int ii : w) {
        minLength = Math.min(minLength, length(i, ii));
      }
    }
    return minLength;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("Ancestor arguments wrong");
    }
    int minLength = Integer.MAX_VALUE;
    int ancestor = -1;
    for (int i: v) {
      for (int ii : w) {
        int length = length(i, ii);
        if (length < minLength) {
          ancestor = ancestor(i, ii);
          minLength = length;
        }

      }
    }
    return ancestor;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    Digraph G = new Digraph(new In(args[0]));
    SAP sap = new SAP(G);
    StdOut.println(sap.length(6 , 2));
    StdOut.println(sap.length(2 , 6));
    StdOut.println(sap.length(2 , 6));
//    StdOut.println(sap.ancestor(7, 2));
  }
}