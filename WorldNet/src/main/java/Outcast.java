import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private final WordNet wordnet;
  public Outcast(WordNet wordnet) {
    if (wordnet == null) {
      throw new IllegalArgumentException("Muss pass a WordNet");
    }
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {
    String outcast = "";
    int maxDistance = 0;
    for (String nounA: nouns) {
      int distance = 0;
      for (String nounB : nouns) {
        if (nounA.equals(nounB)) {
          continue;
        }
        distance += wordnet.distance(nounA.trim(), nounB.trim());
      }
      if (distance > maxDistance) {
        outcast = nounA;
        maxDistance = distance;
      }
    }
    return outcast;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}
