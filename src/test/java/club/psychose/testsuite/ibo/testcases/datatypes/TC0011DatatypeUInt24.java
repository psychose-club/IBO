package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt24;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TC0011DatatypeUInt24 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt24(UInt24.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt24(UInt24.getMaximumValue() + 1));

        // Storing and fetching values.
        UInt24 uInt24 = new UInt24(1337);
        UInt24 secondUInt16 = new UInt24(2222);

        int storedValue = uInt24.getValue();
        assertEquals(storedValue, 1337);
        storedValue = 1231;
        assertEquals(storedValue, 1231);

        uInt24.setValue(2222);
        assertEquals(uInt24.getValue(), 2222);
        assertEquals(uInt24.getValue(), secondUInt16.getValue());
        assertEquals(uInt24.toString(), "2222");

        String hexString = uInt24.getAsHEXString(HEXFormat.LOWERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals(hexString, "ae0800");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = new byte[3];

        boolean useBigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        if (useBigEndian) {
            bytesWithoutSetByteOrder[1] = 0x50;
            bytesWithoutSetByteOrder[2] = 0x57;
        } else {
            bytesWithoutSetByteOrder[0] = 0x57;
            bytesWithoutSetByteOrder[1] = 0x50;
        }

        assertEquals(new UInt24(bytesWithoutSetByteOrder).getValue(), 20567);

        // We will reverse the check now to check both ByteOrders.
        useBigEndian = !(useBigEndian);

        byte[] bytesWithByteOrder = new byte[9];

        if (useBigEndian) {
            bytesWithByteOrder[1] = 0x26;
            bytesWithByteOrder[2] = (byte) 0xF6;
        } else {
            bytesWithByteOrder[0] = (byte) 0xF6;
            bytesWithByteOrder[1] = 0x26;
        }

        assertEquals(new UInt24(bytesWithByteOrder, (useBigEndian) ? (ByteOrder.BIG_ENDIAN) : (ByteOrder.LITTLE_ENDIAN)).getValue(), 9974);
        assertEquals(new UInt24((byte) 50).getValue(), 50);
        assertEquals(new UInt24((short) 1541).getValue(), 1541);
        assertEquals(new UInt24((long) 16777215).getValue(), 16777215);
        assertEquals(new UInt24(2145f).getValue(), 2145);
        assertEquals(new UInt24(0.45).getValue(), 0);
        assertEquals(new UInt24(BigInteger.valueOf(99654)).getValue(), 99654);
        assertEquals(new UInt24("0").getValue(), 0);
    }
}