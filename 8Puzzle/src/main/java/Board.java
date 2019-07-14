import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private final int size;
    private final int[][] board;
    private Board twinBoard;

    /**
     *
     */
    private class Coordinate {
        private int row;
        private int col;

        Coordinate(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "[" + row + "-" + col + "]";
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Coordinate)) {
                return false;
            }
            Coordinate coordinateObj = (Coordinate) obj;
            if (this.row == coordinateObj.row && this.col == coordinateObj.col) {
                return true;
            }
            return false;
        }

        public int getDistance(int otherRow, int otherCol) {
            return Math.abs(this.row - otherRow) + Math.abs(this.col - otherCol);
        }
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        size = tiles.length;
        this.board = cloneTiles(tiles);
    }

    private int[][] cloneTiles(int[][] oldTiles) {
        int[][] newTiles = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newTiles[row][col] = oldTiles[row][col];
            }
        }
        return newTiles;
    }
    // string representation of this board'
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(size);
        builder.append('\n');
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                builder.append(board[row][col]);
                if (col < size - 1) {
                    builder.append(" ");
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == size - 1 && col == size - 1) {
                    continue;
                }
                if ((board[row][col] != getSolutionValue(row, col))) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getSolutionValue(int row, int col) {
        if (row == size - 1 && col == size - 1) {
            return 0;
        }
        return row * size + col + 1;
    }

    private Coordinate getSolutionPosition(int value) {
        if (value == 0) {
            return new Coordinate(size - 1, size - 1);
        }
        if ( value % size == 0) {
            return new Coordinate(value / size - 1,  size - 1);
        }
        return new Coordinate(( value / size), ( value % size) - 1);
    }

    private Coordinate getPosition(int value) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.board[row][col] == value) {
                    return new Coordinate(row, col);
                }
            }
        }
        throw new IllegalArgumentException("value not present");
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!getSolutionPosition(board[row][col]).equals(new Coordinate(row, col)) && board[row][col] != 0) {
                    count = count + getSolutionPosition(board[row][col]).getDistance(row, col);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if ((board[row][col] != getSolutionValue(row, col))) {
                    return false;
                }

            }
        }
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || !(other.getClass() == this.getClass())) {
            return false;
        }
        Board otherBoard = (Board) other;
        if (this.size != otherBoard.dimension()) {
            return false;
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.board[row][col] != otherBoard.board[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private Board getSwappedBoard(Coordinate source, Coordinate destination) {
        int[][] newTiles = cloneTiles(this.board);
        int sourceValue = this.board[source.row][source.col];

        int destinationValue = this.board[destination.row][destination.col];
        newTiles[destination.row][destination.col] = sourceValue;
        newTiles[source.row][source.col] = destinationValue;

        return new Board(newTiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        Coordinate zeroPosition = getPosition(0);
        if (zeroPosition.row > 0) {
            neighbors.add(getSwappedBoard(new Coordinate(zeroPosition.row, zeroPosition.col), new Coordinate(zeroPosition.row - 1, zeroPosition.col)));
        }
        if (zeroPosition.row < size - 1) {
            neighbors.add(getSwappedBoard(new Coordinate(zeroPosition.row, zeroPosition.col), new Coordinate(zeroPosition.row + 1, zeroPosition.col)));

        }
        if (zeroPosition.col > 0) {
            neighbors.add(getSwappedBoard(zeroPosition, new Coordinate(zeroPosition.row, zeroPosition.col - 1)));
        }
        if (zeroPosition.col < size - 1) {
            neighbors.add(getSwappedBoard(new Coordinate(zeroPosition.row, zeroPosition.col), new Coordinate(zeroPosition.row, zeroPosition.col + 1)));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    private Board getTwin() {
        Random random = new Random();
        int row = random.nextInt(size);
        int col = random.nextInt(size);
        while (board[row][col] == 0) {
            row = random.nextInt(size);
            col = random.nextInt(size);
        }

        if (row > 0 && board[row - 1][col] != 0) {
            return getSwappedBoard(new Coordinate(row, col), new Coordinate(row - 1, col));
        }
        if (row < size - 1 && board[row + 1][col] != 0) {
            return getSwappedBoard(new Coordinate(row, col), new Coordinate(row + 1, col));
        }
        if (col > 0 && board[row][col - 1] != 0) {
            return getSwappedBoard(new Coordinate(row, col), new Coordinate(row, col - 1));
        }
        return getSwappedBoard(new Coordinate(row, col), new Coordinate(row, col + 1));
    }

    public Board twin() {
        if (twinBoard == null) {
            twinBoard = getTwin();
        }

        return twinBoard;

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
           StdOut.println(initial.manhattan());
        }
    }

}