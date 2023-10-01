package club.psychose.testsuite.ibo.testcases.utils;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.library.ibo.utils.BitUtils;
import club.psychose.testsuite.ibo.testcases.Test;

public final class TC0012BitUtils extends Test {
    public TC0012BitUtils () {
        super("TC_0012_UTILS_BITUTILS");
    }

    @Override
    public void executeTestCase () {
        Int8 int8;
        UInt8 uInt8;
        Int16 int16;
        UInt16 uInt16;
        Int24 int24;
        UInt24 uInt24;
        Int32 int32;
        UInt32 uInt32;
        Int64 int64;
        UInt64 uInt64;
        byte testByte;
        short testShort;
        int testInt;
        long testLong;

        try {
            // Initializing all required variables.
            int8 = new Int8(127);
            uInt8 = new UInt8(10);
            int16 = new Int16(160);
            uInt16 = new UInt16(34);
            int24 = new Int24(12);
            uInt24 = new UInt24(555);
            int32 = new Int32(4000);
            uInt32 = new UInt32(9000);
            int64 = new Int64(-180);
            uInt64 = new UInt64(69999);
            testByte = (byte) 12;
            testShort = (short) 4154;
            testInt = 89494;
            testLong = -623L;
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("INITIALIZATION_FAILED");
            return;
        }

        try {
            BitUtils.extractBits(int8, 0, -1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {
        }

        try {
            BitUtils.extractBits(int8, -1, 0);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {
        }

        try {
            BitUtils.extractBits(int8, 0, Int8.getBitLength() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {
        }

        try {
            long int8BitResult = BitUtils.extractBits(int8, 0, 2);

            // The result should be 0000 0111 (7)
            if (int8BitResult != 7) {
                this.failed("INVALID_INT8_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long uInt8BitResult = BitUtils.extractBits(uInt8, 0, 0);

            // The result should be 0000 0000 (0)
            if (uInt8BitResult != 0) {
                this.failed("INVALID_UINT8_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long int16BitResult = BitUtils.extractBits(int16, 0, 1);

            // The result should be 0000 0000 0000 0000 (0)
            if (int16BitResult != 0) {
                this.failed("INVALID_INT16_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long uInt16BitResult = BitUtils.extractBits(uInt16, 0, 4);

            // The result should be 0000 0000 0000 0010 (2)
            if (uInt16BitResult != 2) {
                this.failed("INVALID_UINT16_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long int24BitResult = BitUtils.extractBits(int24, 0, 8);

            // The result should be 0000 0000 0000 0000 0000 1100 (12)
            if (int24BitResult != 12) {
                this.failed("INVALID_INT24_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long uInt24BitResult = BitUtils.extractBits(uInt24, 0, 0);

            // The result should be 0000 0000 0000 0000 0000 0001 (1)
            if (uInt24BitResult != 1) {
                this.failed("INVALID_UINT24_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long int32BitResult = BitUtils.extractBits(int32, 0, 1);

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 (0)
            if (int32BitResult != 0) {
                this.failed("INVALID_INT32_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long uInt32BitResult = BitUtils.extractBits(uInt32, 0, 8);

            // The result should be 0000 0000 0000 0000 0000 0001 0010 1000 (296)
            if (uInt32BitResult != 296) {
                this.failed("INVALID_UINT32_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long int64BitResult = -(BitUtils.extractBits(int64, 0, 2));

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0100 (4)
            if (int64BitResult != -4) {
                this.failed("INVALID_INT64_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long uInt64BitResult = BitUtils.extractBits(uInt64, 0, 9);

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001 0110 1111 (367)
            if (uInt64BitResult != 367) {
                this.failed("INVALID_UINT64_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long byteBitResult = BitUtils.extractBits(testByte, 0, 5);

            // The result should be 0000 1100 (7)
            if (byteBitResult != 12) {
                this.failed("INVALID_BYTE_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long shortBitResult = BitUtils.extractBits(testShort, 0, 8);

            // The result should be 0000 0000 0011 1010 (58)
            if (shortBitResult != 58) {
                this.failed("INVALID_SHORT_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long intBitResult = BitUtils.extractBits(testInt, 0, 6);

            // The result should be 0000 0000 0001 0110 (22)
            if (intBitResult != 22) {
                this.failed("INVALID_INT_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        try {
            long longBitResult = -(BitUtils.extractBits(testLong, 0, 0));

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001 (1)
            if (longBitResult != -1) {
                this.failed("INVALID_INT_BIT_RESULT");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS");
            return;
        }

        this.passed();
    }
}