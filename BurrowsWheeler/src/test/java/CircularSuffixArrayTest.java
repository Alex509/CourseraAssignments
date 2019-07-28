import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircularSuffixArrayTest {
    private static final CircularSuffixArray ar = new CircularSuffixArray("ABRACADABRA!");


    @Test
    void length() {
        assertEquals(12, ar.length());
    }

    @Test
    void index() {
        assertEquals(2, ar.index(11));
    }

    @Test
    @DisplayName("expects to throw correct exception")
    void indexInvalidInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ar.index(-3));
    }

    @Test
    @DisplayName("empty constructor argument")
    void testConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CircularSuffixArray(null));
    }

    @Test
    @DisplayName("complicated string")
    void testStars() {
        CircularSuffixArray array = new CircularSuffixArray("*************");
        assertEquals(0, array.index(1));
    }
}