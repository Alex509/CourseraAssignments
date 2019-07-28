import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BurrowsWheeler {
    private static final int RADIX = 8;
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        final String str = BinaryStdIn.readString();
        BinaryStdIn.close();
        final CircularSuffixArray suffixArray = new CircularSuffixArray(str);
        char[] lastColumnT = new char[str.length()];
        int first = 0;
        for (int i = 0; i < str.length(); i++) {
            final int shift = suffixArray.index(i);
            lastColumnT[i] = lastForShift(str, shift);
            if (shift == 0) {
                first = i;
            }
         }
        BinaryStdOut.write(first);
        for (final char c : lastColumnT) {
            BinaryStdOut.write(c, RADIX);
        }
        BinaryStdOut.close();
    }

    private static char lastForShift(final String str, final int shift) {
        return shift > 0 ? str.charAt(shift - 1) : str.charAt(str.length() - 1);
    }




    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        final int originalIndex = BinaryStdIn.readInt();
        final List<Integer> lastColumnT = BinaryStdIn.readString().chars().boxed().collect(Collectors.toList());
        BinaryStdIn.close();
        final ArrayList<Integer> first = new ArrayList<>(lastColumnT);
        final ArrayList<Integer> next = new ArrayList<>();

        final HashMap<Integer, List<Integer>> lookupMap = new HashMap<>();
        for (int i = 0; i < lastColumnT.size(); i++) {
            if (lookupMap.get(lastColumnT.get(i)) == null) {
                lookupMap.put(lastColumnT.get(i), new ArrayList<>());
            }
            lookupMap.get(lastColumnT.get(i)).add(i);
        }
        first.sort(Comparator.naturalOrder());
        int index = 0;
        while (index < lastColumnT.size()) {
            if (lookupMap.get(first.get(index)).size() == 1) {
                next.add(lookupMap.get(first.get(index)).get(0));
                index++;
            } else {
                lookupMap.get(first.get(index)).forEach(next::add);
                index += lookupMap.get(first.get(index)).size();
            }
        }
        int i = 1;
        BinaryStdOut.write(first.get(originalIndex), RADIX);
        int nextChar = next.get(originalIndex);
        while (i < first.size()) {
            BinaryStdOut.write(first.get(nextChar), RADIX);
            nextChar = next.get(nextChar);
            i++;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(final String[] args) {
        if (args[0] == null) {
            throw new IllegalArgumentException("provide the sign and filename to read from");
        }
        if (args[0].equals("+")) {
            inverseTransform();
        } else {
            transform();
        }
    }

}