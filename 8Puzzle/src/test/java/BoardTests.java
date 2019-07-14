import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTests {
    @Test
    void argumentString() {
        int[][] tiles = new int[][]{{1, 3}, {2, 5}};
        Board board = new Board(tiles);
        assertEquals(board.equals("a"), false);
    }

    @Test
    void nullArgument() {
        int[][] tiles = new int[][]{{1, 3}, {2, 5}};
        Board board = new Board(tiles);
        assertEquals(board.equals(null), false);
    }

    @Test
    void mutateArgument() {
        int[][] tiles = new int[][]{{1, 3}, {2, 5}};
        Board board = new Board(tiles);
        assertEquals(board.hamming(), 2);
        tiles[0] = new int[]{ 3, 1};
        assertEquals(board.hamming(), 2);
    }
}
