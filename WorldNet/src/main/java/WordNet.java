import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WordNet {
  private final HashMap<String, List<Integer>> synsets;
  private final HashMap<Integer, String> synsetsLookup;
  private final Digraph G;

  // constructor takes the name of the two input files
  public WordNet(String synsetsUrl, String hypernymsUrl) {
    if (synsetsUrl == null || hypernymsUrl == null) {
      throw new IllegalArgumentException("Illegal Arguments");
    }
    synsets = new HashMap<>();
    synsetsLookup = new HashMap<>();
    In in = new In(synsetsUrl);
    for (String line : in.readAllLines()) {
      for (String key :line.split(",")[1].trim().split(" ")) {
        if (!synsets.containsKey(key)) {
          synsets.put(key, new ArrayList<Integer>());
        }
        synsets.get(key).add(Integer.parseInt(line.split(",")[0]));
      }
      synsetsLookup.put(Integer.parseInt(line.split(",")[0]), line.split(",")[1].trim());
    }
    in.close();
    in = new In(hypernymsUrl);
    String[] hypernyms = in.readAllLines();
    G = new Digraph(hypernyms.length + 1);
    for (String line : hypernyms) {
      String[] arr = line.split(",");
      for (int i = 1; i < arr.length; i++) {
        G.addEdge(Integer.parseInt(arr[0]), Integer.parseInt(arr[i]));
      }
    }
    Topological topological = new Topological(G);
    if(!topological.hasOrder()) {
      throw new IllegalArgumentException("Digraph has cycle");
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return synsets.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("Invalid arguments");
    }
    return synsets.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null || !synsets.containsKey(nounA) || !synsets.containsKey(nounB)) {
      throw new IllegalArgumentException("Invalid arguments " + nounA + " | " + nounB + " | ");
    }
    Iterable<Integer> vertexA = synsets.get(nounA);
    Iterable<Integer> vertexB = synsets.get(nounB);
    BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(G, vertexA);
    BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(G, vertexB);
    int maxDistance = Integer.MAX_VALUE;
    for (int vertex : synsetsLookup.keySet()) {
      if (pathsA.hasPathTo(vertex) && pathsB.hasPathTo(vertex)) {
        int currentDistance = pathsA.distTo(vertex) + pathsB.distTo(vertex);
        if (currentDistance < maxDistance) {
          maxDistance = currentDistance;
        }
      }
    }
    return maxDistance == Integer.MAX_VALUE ? -1 : maxDistance;
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("Invalid arguments");
    }
    SAP sap = new SAP(G);
    Iterable<Integer> vertexA = synsets.get(nounA);
    Iterable<Integer> vertexB = synsets.get(nounB);
    return synsetsLookup.get(sap.ancestor(vertexA, vertexB));
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wn = new WordNet(args[0], args[1]);
  }
}