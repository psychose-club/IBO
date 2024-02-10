package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.signed.Int24;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TC0010DatatypeInt24 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new Int24(Int24.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new Int24(Int24.getMaximumValue() + 1));

        // Storing and fetching values.
        Int24 int16 = new Int24(9788);
        Int24 secondInt16 = new Int24(121);

        int storedValue = int16.getValue();
        assertEquals(storedValue, 9788);
        storedValue = 1432;
        assertEquals(storedValue, 1432);

        int16.setValue(121);
        assertEquals(int16.getValue(), 121);
        assertEquals(int16.getValue(), secondInt16.getValue());
        assertEquals(int16.toString(), "121");

        String hexString = int16.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);
        assertEquals(hexString, "000079");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = new byte[3];

        boolean useBigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        if (useBigEndian) {
            bytesWithoutSetByteOrder[0] = (byte) 0xFF;
            bytesWithoutSetByteOrder[1] = (byte) 0xFC;
            bytesWithoutSetByteOrder[2] = 0x2E;
        } else {
            bytesWithoutSetByteOrder[0] = 0x2E;
            bytesWithoutSetByteOrder[1] = (byte) 0xFC;
            bytesWithoutSetByteOrder[2] = (byte) 0xFF;
        }

        assertEquals(new Int24(bytesWithoutSetByteOrder).getValue(), -978);

        // We will reverse the check now to check both ByteOrders.
        useBigEndian = !(useBigEndian);

        byte[] bytesWithByteOrder = new byte[32];

        if (useBigEndian) {
            bytesWithByteOrder[0] = (byte) 0xFF;
            bytesWithByteOrder[1] = (byte) 0xFE;
            bytesWithByteOrder[2] = 0x6C;
        } else {
            bytesWithByteOrder[0] = 0x6C;
            bytesWithByteOrder[1] = (byte) 0xFE;
            bytesWithByteOrder[2] = (byte) 0xFF;
        }

        assertEquals(new Int24(bytesWithByteOrder, (useBigEndian) ? (ByteOrder.BIG_ENDIAN) : (ByteOrder.LITTLE_ENDIAN)).getValue(), -404);
        assertEquals(new Int24((byte) 3).getValue(), 3);
        assertEquals(new Int24((short) 74).getValue(), 74);
        assertEquals(new Int24((long) 8388607).getValue(), 8388607);
        assertEquals(new Int24(2231f).getValue(), 2231);
        assertEquals(new Int24(6.3).getValue(), 6);
        assertEquals(new Int24(BigInteger.valueOf(9)).getValue(), 9);
        assertEquals(new Int24("1").getValue(), 1);
    }
}