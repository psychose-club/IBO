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
import club.psychose.library.ibo.utils.HEXUtils;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class TC0010WriterBinaryWriterChunkPadding extends Test {
    public TC0010WriterBinaryWriterChunkPadding () {
        super("TC_0010_WRITER_BINARYWRITER_CHUNKPADDING");
    }

    @Override
    public void executeTestCase () {
        BinaryWriter binaryWriter = new BinaryWriter(ByteOrder.LITTLE_ENDIAN);
        binaryWriter.enableChunkPadding(0xA, (byte) 0x0);

        if (!(binaryWriter.isChunkPaddingEnabled())) {
            this.failed("CHUNK_PADDING_IS_NOT_ENABLED");
            return;
        }

        if (binaryWriter.getChunkSize() != 0xA) {
            this.failed("CHUNK_SIZE_IS_INVALID");
            return;
        }

        Int8 int8;
        Int16 int16;
        Int32 int32;
        Int64 int64;
        UInt8 uInt8;
        UInt16 uInt16;
        UInt32 uInt32;
        UInt64 uInt64;
        float binaryFloat = 4463f;
        double binaryDouble = 15.246d;
        String binaryString = "Hello <3";
        byte[] binaryBytes;

        try {
            int8 = new Int8("64");
            int16 = new Int16(-32167);
            int32 = new Int32(1222546);
            int64 = new Int64("-999999745");
            uInt8 = new UInt8("154");
            uInt16 = new UInt16(64967);
            uInt32 = new UInt32(91512);
            uInt64 = new UInt64("44465415241185");

            binaryBytes = new byte[4];
            binaryBytes[0] = 0x69;
            binaryBytes[1] = 0x11;
            binaryBytes[2] = (byte) 0x87;
            binaryBytes[3] = (byte) 0x4D;
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STRUCTURES_RANGE_OUT_OF_BOUNDS");
            rangeOutOfBoundsException.printStackTrace();
            return;
        }

        try {
            binaryWriter.open(PathUtils.getTestSuiteFolderPath("\\test.bin"), true);

            if (binaryWriter.isClosed()) {
                this.failed("BINARY_WRITER_CLOSED");
                return;
            }

            binaryWriter.setOffsetPosition(0);
            binaryWriter.fill((byte) 0xF3, 0x4);
            binaryWriter.fillOnly((byte) 0x4, 0x8);

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
            binaryWriter.write(binaryString, StandardCharsets.US_ASCII);

            binaryWriter.disableChunkPadding();
            binaryWriter.write(binaryBytes);

            long fileLength = binaryWriter.getFileLength();
            binaryWriter.close();

            if (fileLength != 0x84) {
                binaryWriter.close();
                this.failed("FILE_LENGTH_INVALID");
                return;
            }

            byte[] fileBytes = Files.readAllBytes(PathUtils.getTestSuiteFolderPath("\\test.bin"));
            Files.deleteIfExists(PathUtils.getTestSuiteFolderPath("\\test.bin"));

            String hexString = HEXUtils.convertBytesToHEXString(fileBytes);

            if (!(hexString.equals("F3F3F3F30000000000000404040404040404400000000000000000005982000000000000000092A71200000000000000FF3665C4FFFFFFFF00009A000000000000000000C7FD000000000000000078650100000000000000E1BD47E970280000000000788B45000000000000CBA145B6F37D2E40000048656C6C6F203C3300006911874D"))) {
                binaryWriter.close();
                this.failed("HEX_CHECK");
                return;
            }

            binaryWriter.close();
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