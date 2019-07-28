import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BurrowsWheelerTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private byte[] encodedByteArray;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

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

    private byte[] convertHexToByte(String s) {
        byte[] val = new byte[s.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(s.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }

    @Test
    void transform() {
        final String testString = "ABRACADABRA!";
        provideInput(testString);
        BurrowsWheeler.transform();
        String str = "00000003415244215243414141414242";
        assertArrayEquals(convertHexToByte(str), testOut.toByteArray());

    }

    @Test
    void inverseTransformSimilar() {
        final String hexString = "000000004141414141414141414141";
        provideInput(convertHexToByte(hexString));
        BurrowsWheeler.inverseTransform();
        Assertions.assertEquals("AAAAAAAAAAA", testOut.toString());
        restoreSystemInputOutput();
    }

    @Test
    void transformSimilar() {
        final String hexString = "000000004141414141414141414141";
        provideInput("AAAAAAAAAAA");
        BurrowsWheeler.transform();
        Assertions.assertArrayEquals(convertHexToByte(hexString), testOut.toByteArray());
        restoreSystemInputOutput();
    }

    @Test
    void inverseTransform() {
        final String hexString = "00000003415244215243414141414242";
        provideInput(convertHexToByte(hexString));
        BurrowsWheeler.inverseTransform();
        Assertions.assertEquals("ABRACADABRA!", testOut.toString());
        restoreSystemInputOutput();
    }

    @Test
    void inverseTransformBothWays() {
        final String testString = "AAAAAAAAAA";
        provideInput(testString);
        BurrowsWheeler.transform();
        byte[] input = testOut.toByteArray().clone();
        setUpOutput();
        provideInput(input);
        BurrowsWheeler.inverseTransform();
        Assertions.assertEquals("AAAAAAAAAA", testOut.toString());
    }
}