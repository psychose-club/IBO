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

package club.psychose.testsuite.ibo.testcases.writer;

import club.psychose.library.ibo.BinaryWriter;
import club.psychose.library.ibo.datatypes.types.signed.Int16;
import club.psychose.library.ibo.datatypes.types.signed.Int32;
import club.psychose.library.ibo.datatypes.types.signed.Int64;
import club.psychose.library.ibo.datatypes.types.signed.Int8;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.HEXUtils;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;

public class TC0009WriterBinaryWriterNoChunkPadding extends Test {
    public TC0009WriterBinaryWriterNoChunkPadding() {
        super("TC_0009_WRITER_BINARYWRITER_NOCHUNKPADDING");
    }

    @Override
    public void executeTestCase () {
        BinaryWriter binaryWriter = new BinaryWriter();
        binaryWriter.setByteOrder(ByteOrder.BIG_ENDIAN);

        Int8 int8;
        Int16 int16;
        Int32 int32;
        Int64 int64;
        UInt8 uInt8;
        UInt16 uInt16;
        UInt32 uInt32;
        UInt64 uInt64;
        float binaryFloat = 235f;
        double binaryDouble = 5934d;
        String binaryString = "This is a test string!";
        byte[] binaryBytes;

        try {
            int8 = new Int8("1");
            int16 = new Int16(6263);
            int32 = new Int32(-853857);
            int64 = new Int64("-420");
            uInt8 = new UInt8("2");
            uInt16 = new UInt16(24);
            uInt32 = new UInt32(55);
            uInt64 = new UInt64(48244);

            binaryBytes = new byte[3];
            binaryBytes[0] = 0x40;
            binaryBytes[1] = (byte) 0x94;
            binaryBytes[2] = 0x13;
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STRUCTURES_RANGE_OUT_OF_BOUNDS");
            rangeOutOfBoundsException.printStackTrace();
            return;
        }

        try {
            binaryWriter.open(PathUtils.getTestSuiteFolderPath("\\test.bin"), true);

            binaryWriter.setOffsetPosition(0);
            binaryWriter.fill((byte) 0x0, 0xA);

            binaryWriter.write(int8);
            binaryWriter.write(int16);
            binaryWriter.write(int32);
            binaryWriter.write(int64);
            binaryWriter.write(uInt8);
            binaryWriter.write(uInt16);
            binaryWriter.write(uInt32);
            binaryWriter.write(uInt64);
            binaryWriter.write(binaryFloat);
            binaryWriter.write(binaryDouble);
            binaryWriter.write(binaryString);
            binaryWriter.write(binaryBytes);
            long offset = binaryWriter.getOffsetPosition();
            binaryWriter.close();

            if (offset != 0x4D) {
                this.failed("OFFSET_INVALID");
                return;
            }

            byte[] fileBytes = Files.readAllBytes(PathUtils.getTestSuiteFolderPath("\\test.bin"));
            Files.deleteIfExists(PathUtils.getTestSuiteFolderPath("\\test.bin"));

            String hexString = HEXUtils.convertBytesToHEXString(fileBytes);

            if (!(hexString.equals("00000000000000000000011877FFF2F89FFFFFFFFFFFFFFE5C02001800000037000000000000BC74436B000040B72E0000000000546869732069732061207465737420737472696E6721409413"))) {
                this.failed("HEX_CHECK");
                return;
            }

            this.passed();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (IOException ioException) {
            this.failed("IO_EXCEPTION");
            ioException.printStackTrace();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS_EXCEPTION");
            rangeOutOfBoundsException.printStackTrace();
        }
    }
}