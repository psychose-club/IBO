package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.signed.Int24;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

public final class TC0010DatatypeInt24 extends Test {
    public TC0010DatatypeInt24() {
        super("TC_0010_DATATYPE_INT24");
    }

    @Override
    public void executeTestCase () {
        // Out-of-Bounds Test.
        try {
            new Int24(Int24.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new Int24(Int24.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            Int24 int24 = new Int24(9788);
            int storedValue = int24.getValue();

            if (storedValue != 9788) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 1432;

            if (int24.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            int24.setValue(121);
            Int24 secondInt24 = new Int24(121);

            if (int24.getValue() != 121) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(int24.equals(secondInt24))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = int24.getAsString();
            if (!(valueAsString.equals("121"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = int24.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);

            if (!(hexString.equals("000079"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }

            // Check the other constructors.
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

            if (new Int24(bytesWithoutSetByteOrder).getValue() != -978) {
                this.failed("OTHER_CONSTRUCTORS_01");
                return;
            }

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

            if (new Int24(bytesWithByteOrder, (useBigEndian) ? (ByteOrder.BIG_ENDIAN) : (ByteOrder.LITTLE_ENDIAN)).getValue() != -404) {
                this.failed("OTHER_CONSTRUCTORS_02");
                return;
            }

            if (new Int24((byte) 3).getValue() != 3) {
                this.failed("OTHER_CONSTRUCTORS_03");
                return;
            }

            if (new Int24((short) 74).getValue() != 74) {
                this.failed("OTHER_CONSTRUCTORS_04");
                return;
            }

            if (new Int24((long) 8388607).getValue() != 8388607) {
                this.failed("OTHER_CONSTRUCTORS_05");
                return;
            }

            if (new Int24(2231f).getValue() != 2231) {
                this.failed("OTHER_CONSTRUCTORS_06");
                return;
            }

            if (new Int24(6.3).getValue() != 6) {
                this.failed("OTHER_CONSTRUCTORS_07");
                return;
            }

            if (new Int24(BigInteger.valueOf(9)).getValue() != 9) {
                this.failed("OTHER_CONSTRUCTORS_08");
                return;
            }

            if (new Int24("1").getValue() != 1) {
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