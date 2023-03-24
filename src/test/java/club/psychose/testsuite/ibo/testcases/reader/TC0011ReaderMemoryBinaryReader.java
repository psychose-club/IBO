/*
 * Copyright (c) 2023, psychose.club
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package club.psychose.testsuite.ibo.testcases.reader;

import club.psychose.library.ibo.MemoryBinaryReader;
import club.psychose.library.ibo.datatypes.types.signed.Int16;
import club.psychose.library.ibo.datatypes.types.signed.Int32;
import club.psychose.library.ibo.datatypes.types.signed.Int8;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.library.ibo.utils.HEXUtils;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

public final class TC0011ReaderMemoryBinaryReader extends Test {
    public TC0011ReaderMemoryBinaryReader () {
        super("TC_0011_READER_MEMORYBINARYREADER");
    }

    @Override
    public void executeTestCase () {
        byte[] memoryBytes = this.getMemoryBytes();
        String hexString = HEXUtils.convertBytesToHEXString(memoryBytes);

        if (!(hexString.equals("00000000000C0416A220B0FF5E5C066FFEFFFFFFFCA38933159F31BAF7A58FCA280100A473584A1BF5101FCE9A5841FFFFFFFF506C6561736520646F6E2774206F766572666C6F77203A63"))) {
            this.failed("FAILED_TO_READ_MEMORY_BYTES");
            return;
        }

        MemoryBinaryReader memoryBinaryReader = new MemoryBinaryReader(ByteOrder.LITTLE_ENDIAN);

        try {
            memoryBinaryReader.open(memoryBytes, 5);

            if (memoryBinaryReader.getBinaryLength() != 0x4B) {
                this.failed("INVALID_BINARY_LENGTH");
                return;
            }

            Int8 int8 = memoryBinaryReader.readInt8();

            if (int8.getValue() != 12) {
                this.failed("FAILED_TO_READ_INT8");
                return;
            }

            Int16 int16 = memoryBinaryReader.readInt16();

            if (int16.getValue() != 5636) {
                this.failed("FAILED_TO_READ_INT16");
                return;
            }

            int offsetPosition = memoryBinaryReader.getOffsetPosition();

            if (offsetPosition != 0x08) {
                this.failed("INVALID_OFFSET");
                return;
            }

            Int32 int32 = memoryBinaryReader.readInt32();

            if (int32.getValue() != -5234526) {
                this.failed("FAILED_TO_READ_INT32");
                return;
            }

            BigInteger int64 = memoryBinaryReader.readInt64().getValue();

            if (int64.compareTo(BigInteger.valueOf(-6727246754L)) != 0) {
                this.failed("FAILED_TO_READ_INT64");
                return;
            }

            UInt8 uInt8 = memoryBinaryReader.readUInt8();

            if (uInt8.getValue() != 252) {
                this.failed("FAILED_TO_READ_UINT8");
                return;
            }

            UInt16 uInt16 = memoryBinaryReader.readUInt16();

            if (uInt16.getValue() != 35235) {
                this.failed("FAILED_TO_READ_UINT16");
                return;
            }

            UInt32 uInt32 = memoryBinaryReader.readUInt32();

            if (uInt32.getValue() != 832509235) {
                this.failed("FAILED_TO_READ_UINT32");
                return;
            }

            BigInteger uInt64 = memoryBinaryReader.readUInt64().getValue();

            if (!(uInt64.equals(BigInteger.valueOf(326325435234234L)))) {
                this.failed("FAILED_TO_READ_UINT64");
                return;
            }

            float binaryFloat = memoryBinaryReader.readFloat();

            if (binaryFloat != 3546345F) {
                this.failed("FAILED_TO_READ_FLOAT");
                return;
            }

            double binaryDouble = memoryBinaryReader.readDouble();

            if (binaryDouble != 6449976.48541d) {
                this.failed("FAILED_TO_READ_DOUBLE");
                return;
            }

            memoryBinaryReader.skipOffsetPosition(4);

            String binaryString = memoryBinaryReader.readString(24);

            if (!(binaryString.equals("Please don't overflow :c"))) {
                this.failed("FAILED_TO_READ_STRING");
                return;
            }

            this.passed();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS_EXCEPTION");
            rangeOutOfBoundsException.printStackTrace();
        } finally {
            memoryBinaryReader.close();
        }
    }

    private byte[] getMemoryBytes () {
        byte[] memoryBytes = new byte[75];

        // Bytes that we skip.
        memoryBytes[0] = 0x0;
        memoryBytes[1] = 0x0;
        memoryBytes[2] = 0x0;
        memoryBytes[3] = 0x0;
        memoryBytes[4] = 0x0;

        // Int8 = 12
        memoryBytes[5] = 0x0C;

        // Int16 = 5636
        memoryBytes[6] = 0x04;
        memoryBytes[7] = 0x16;

        // Int32 = -5234526
        memoryBytes[8] = (byte) 0xA2;
        memoryBytes[9] = 0x20;
        memoryBytes[10] = (byte) 0xB0;
        memoryBytes[11] = (byte) 0xFF;

        // Int64 = -6727246754
        memoryBytes[12] = 0x5E;
        memoryBytes[13] = 0x5C;
        memoryBytes[14] = 0x06;
        memoryBytes[15] = 0x6F;
        memoryBytes[16] = (byte) 0xFE;
        memoryBytes[17] = (byte) 0xFF;
        memoryBytes[18] = (byte) 0xFF;
        memoryBytes[19] = (byte) 0xFF;

        // UInt8 = 252
        memoryBytes[20] = (byte) 0xFC;

        // UInt16 = 35235
        memoryBytes[21] = (byte) 0xA3;
        memoryBytes[22] = (byte) 0x89;

        // UInt32 = 832509235
        memoryBytes[23] = 0x33;
        memoryBytes[24] = 0x15;
        memoryBytes[25] = (byte) 0x9F;
        memoryBytes[26] = 0x31;

        // UInt64 = 326325435234234
        memoryBytes[27] = (byte) 0xBA;
        memoryBytes[28] = (byte) 0xF7;
        memoryBytes[29] = (byte) 0xA5;
        memoryBytes[30] = (byte) 0x8F;
        memoryBytes[31] = (byte) 0xCA;
        memoryBytes[32] = 0x28;
        memoryBytes[33] = 0x01;
        memoryBytes[34] = 0x0;

        // Float = 3546345
        memoryBytes[35] = (byte) 0xA4;
        memoryBytes[36] = 0x73;
        memoryBytes[37] = 0x58;
        memoryBytes[38] = 0x4A;

        // Double = 6449976.48541
        memoryBytes[39] = 0x1B;
        memoryBytes[40] = (byte) 0xF5;
        memoryBytes[41] = 0x10;
        memoryBytes[42] = 0x1F;
        memoryBytes[43] = (byte) 0xCE;
        memoryBytes[44] = (byte) 0x9A;
        memoryBytes[45] = 0x58;
        memoryBytes[46] = 0x41;

        // Bytes that we skip
        memoryBytes[47] = (byte) 0xFF;
        memoryBytes[48] = (byte) 0xFF;
        memoryBytes[49] = (byte) 0xFF;
        memoryBytes[50] = (byte) 0xFF;

        // String = Please don't overflow :c
        memoryBytes[51] = 0x50;
        memoryBytes[52] = 0x6C;
        memoryBytes[53] = 0x65;
        memoryBytes[54] = 0x61;
        memoryBytes[55] = 0x73;
        memoryBytes[56] = 0x65;
        memoryBytes[57] = 0x20;
        memoryBytes[58] = 0x64;
        memoryBytes[59] = 0x6F;
        memoryBytes[60] = 0x6E;
        memoryBytes[61] = 0x27;
        memoryBytes[62] = 0x74;
        memoryBytes[63] = 0x20;
        memoryBytes[64] = 0x6F;
        memoryBytes[65] = 0x76;
        memoryBytes[66] = 0x65;
        memoryBytes[67] = 0x72;
        memoryBytes[68] = 0x66;
        memoryBytes[69] = 0x6C;
        memoryBytes[70] = 0x6F;
        memoryBytes[71] = 0x77;
        memoryBytes[72] = 0x20;
        memoryBytes[73] = 0x3A;
        memoryBytes[74] = 0x63;

        return memoryBytes;
    }
}