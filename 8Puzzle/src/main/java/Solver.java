import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {
    private int moves;
    private ArrayList<Board> solutionPath;
    private MinPQ<SearchNode> heap;
    private MinPQ<SearchNode> twinHeap;

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        private int priority;

        public SearchNode(final Board board, final int moves, final SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previousNode = previousNode;
        }
    }


    private static class BoardComparator implements Comparator<SearchNode> {
        public int compare(final SearchNode left, final SearchNode right) {
            return left.priority - right.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(final Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("cannot be null");
        }
        heap = new MinPQ<>(new BoardComparator());
        twinHeap = new MinPQ<>(new BoardComparator());
        heap.insert(new SearchNode(initial, 0, null));
        twinHeap.insert(new SearchNode(initial.twin(), 0, null));
        while (!heap.min().board.isGoal() && !twinHeap.min().board.isGoal()) {
            final SearchNode dequed = heap.min();
            final SearchNode twinDequed = twinHeap.min();
            heap.delMin();
            twinHeap.delMin();
            dequed.board.neighbors().forEach((Board board) -> {
                if (dequed.previousNode == null || !board.equals(dequed.previousNode.board)) {
                    heap.insert(new SearchNode(board, dequed.moves + 1, dequed));

                }
            });
            twinDequed.board.neighbors().forEach((Board board) -> {
                if (twinDequed.previousNode == null || !board.equals(twinDequed.previousNode.board)) {
                    twinHeap.insert(new SearchNode(board, twinDequed.moves + 1, twinDequed));

                }
            });
        }
        if (heap.min().board.isGoal())  {
            final ArrayList<SearchNode> solutionNodesPath = new ArrayList<>();
            solutionNodesPath.add(heap.min());
            while (true) {
                final SearchNode nextNode = solutionNodesPath.get(solutionNodesPath.size() - 1);
                if (nextNode.previousNode == null) {
                    break;
                }
                solutionNodesPath.add(nextNode.previousNode);
            }
            moves = heap.min().moves;
            solutionPath = solutionNodesPath.stream().map(node -> node.board).collect(Collectors.toCollection(ArrayList::new));
            Collections.reverse(solutionPath);
        } else {
            moves = -1;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return heap.min().board.isGoal();
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutionPath;
    }

    // test client (see below)
    public static void main(final String[] args) {

    }

}