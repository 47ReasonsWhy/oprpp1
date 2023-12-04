package hr.fer.oprpp1.crypto;

import hr.fer.oprpp1.crypto.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

        @Test
        public void testHexToByte() {
            assertArrayEquals(new byte[]{1, -82, 34}, Util.hexToByte("01ae22"));
            assertArrayEquals(new byte[]{1, -82, 34}, Util.hexToByte("01AE22"));
            assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, Util.hexToByte("0000000000000000"));
            assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, Util.hexToByte("ffffffff"));
            assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, Util.hexToByte("0102030405060708"));
            assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', 'f'}, Util.hexToByte("616263646566"));
        }

        @Test
        public void testHexToByteThrows() {
            assertThrows(NullPointerException.class, () -> Util.hexToByte(null));
            assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("01aE2"));
            assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("01aE2g"));
            assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("+01aE2"));
            assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("01aE2-"));
        }

        @Test
        public void testByteToHex() {
            assertEquals("01ae22", Util.byteToHex(new byte[]{1, -82, 34}));
            assertEquals("0000000000000000", Util.byteToHex(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}));
            assertEquals("ffffffff", Util.byteToHex(new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}));
            assertEquals("0102030405060708", Util.byteToHex(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}));
            assertEquals("616263646566", Util.byteToHex(new byte[]{'a', 'b', 'c', 'd', 'e', 'f'}));
        }

        @Test
        public void testByteToHexThrows() {
            assertThrows(NullPointerException.class, () -> Util.byteToHex(null));
        }
}
