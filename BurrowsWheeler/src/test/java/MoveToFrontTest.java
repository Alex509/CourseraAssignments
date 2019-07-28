import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TESTING MOVE TO FRONT")
public class MoveToFrontTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private byte[] encodedByteArray;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        ByteArrayOutputStream bObj = new ByteArrayOutputStream();
        int[] encoded = {65, 66, 82, 2, 68, 1, 69, 1, 4, 4, 2, 38};
        Arrays.stream(encoded).forEach((v) -> {
            bObj.write(v);
        });
        encodedByteArray = bObj.toByteArray();
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
        System.setIn(testIn);
    }

    private void provideInput(byte[] data) {
        testIn = new ByteArrayInputStream(data);
        System.setIn(testIn);
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    @DisplayName("comparing encoded byte arrays")
    public void encode() {
        final String testString = "ABRACADABRA!";
        provideInput(testString);
        MoveToFront.encode();
        assertArrayEquals(encodedByteArray, testOut.toByteArray());
    }

    @Test
    @DisplayName("comparing decoded byte arrays")
    public void decode() {
        provideInput(encodedByteArray);
        MoveToFront.decode();
        assertEquals("ABRACADABRA!", testOut.toString());
    }
}