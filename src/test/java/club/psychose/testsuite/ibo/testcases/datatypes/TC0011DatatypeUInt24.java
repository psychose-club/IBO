package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt24;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

public class TC0011DatatypeUInt24 extends Test {
    public TC0011DatatypeUInt24 () {
        super("TC_0011_DATATYPE_UINT24");
    }

    @Override
    public void executeTestCase () {
        // Out-of-Bounds Test.
        try {
            new UInt24(UInt24.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {
        }

        try {
            new UInt24(UInt24.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {
        }

        // Storing and fetching values.
        try {
            UInt24 uInt24 = new UInt24(1337);
            int storedValue = uInt24.getValue();

            if (storedValue != 1337) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 1231;

            if (uInt24.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            uInt24.setValue(2222);
            UInt24 secondUInt24 = new UInt24(2222);

            if (uInt24.getValue() != 2222) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(uInt24.equals(secondUInt24))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = uInt24.getAsString();
            if (!(valueAsString.equals("2222"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = uInt24.getAsHEXString(HEXFormat.LOWERCASE, ByteOrder.LITTLE_ENDIAN);

            if (!(hexString.equals("ae0800"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }

            // Check the other constructors.
            byte[] bytesWithoutSetByteOrder = new byte[3];

            boolean useBigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
            if (useBigEndian) {
                bytesWithoutSetByteOrder[1] = 0x50;
                bytesWithoutSetByteOrder[2] = 0x57;
            } else {
                bytesWithoutSetByteOrder[0] = 0x57;
                bytesWithoutSetByteOrder[1] = 0x50;
            }

            if (new UInt24(bytesWithoutSetByteOrder).getValue() != 20567) {
                this.failed("OTHER_CONSTRUCTORS_01");
                return;
            }

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

            if (new UInt24(bytesWithByteOrder, (useBigEndian) ? (ByteOrder.BIG_ENDIAN) : (ByteOrder.LITTLE_ENDIAN)).getValue() != 9974) {
                this.failed("OTHER_CONSTRUCTORS_02");
                return;
            }

            if (new UInt24((byte) 50).getValue() != 50) {
                this.failed("OTHER_CONSTRUCTORS_03");
                return;
            }

            if (new UInt24((short) 1541).getValue() != 1541) {
                this.failed("OTHER_CONSTRUCTORS_04");
                return;
            }

            if (new UInt24((long) 16777215).getValue() != 16777215) {
                this.failed("OTHER_CONSTRUCTORS_05");
                return;
            }

            if (new UInt24(2145f).getValue() != 2145) {
                this.failed("OTHER_CONSTRUCTORS_06");
                return;
            }

            if (new UInt24(0.45).getValue() != 0) {
                this.failed("OTHER_CONSTRUCTORS_07");
                return;
            }

            if (new UInt24(BigInteger.valueOf(99654)).getValue() != 99654) {
                this.failed("OTHER_CONSTRUCTORS_08");
                return;
            }

            if (new UInt24("0").getValue() != 0) {
                this.failed("OTHER_CONSTRUCTORS_09");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STORING_AND_FETCHING");
            return;
        }

        this.passed();
    }
}