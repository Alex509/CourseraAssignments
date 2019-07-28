import java.util.Arrays;
import java.util.HashMap;

public class CircularSuffixArray {
    private final String[] arr;
    private final HashMap<String, Integer> map;
    private final int length;
    // circular suffix array of s
    public CircularSuffixArray(final String str) {
        if (str == null) {
            throw new IllegalArgumentException("pls provide an input string");
        }
        length = str.length();
        arr = new String[length()];
        map = new HashMap<>();
        for (int i = 0; i < length(); i++) {
            arr[i] = rotate(str, i);
            if(!map.containsKey(arr[i])) { map.put(arr[i], i); }
        }
        Arrays.sort(arr);
    }

    private static String rotate(final String string, final int offset) {
        final int index = offset % string.length();
        return string.substring(index) + string.substring(0, index);
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(final int index) {
        if (index < 0 || index >= arr.length) {
            throw new IllegalArgumentException("invalid index value");
        }
        return map.get(arr[index]);
    }

    // unit testing (required)
    public static void main(final String[] args) {
    //nothing to do
    }

}