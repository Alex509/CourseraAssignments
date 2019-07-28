import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int ARRAY_SIZE = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        final char[] charTable = getCharTable();
        while (!BinaryStdIn.isEmpty()) {
            final char currentChar = BinaryStdIn.readChar();
            char index = 0;
            for (int j = 0;
                 j < charTable.length;
                 j++, index++) {
                if (currentChar == charTable[j]) {
                    break;
                }
            }
            move(index, charTable, currentChar);
            BinaryStdOut.write(index);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static void move(final int index, char[] charTable, final char characterToMove) {
        if (index != 0) {
            System.arraycopy(charTable, 0, charTable, 1, index);
        }
        charTable[0] = characterToMove;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        final char[] charTable = getCharTable();

        while (!BinaryStdIn.isEmpty()) {
            final char index = BinaryStdIn.readChar();
            BinaryStdOut.write(charTable[index]);
            move(index, charTable, charTable[index]);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static char[] getCharTable() {
        char[] charArray = new char[ARRAY_SIZE];
        for (int i = 0;
             i < ARRAY_SIZE;
             i++) {
            charArray[i] = (char) (0x0 + i);
        }
        return charArray;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(final String[] args) {
        if (args[0] == null) {
            throw new IllegalArgumentException("provide the sign and filename to read from");
        }
        if (args[0].equals("+")) {
            decode();
        } else {
            encode();
        }
    }

}