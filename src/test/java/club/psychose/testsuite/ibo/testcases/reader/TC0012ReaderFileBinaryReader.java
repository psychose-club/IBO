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

import club.psychose.library.ibo.BinaryWriter;
import club.psychose.library.ibo.FileBinaryReader;
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
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class TC0012ReaderFileBinaryReader extends Test {
    public TC0012ReaderFileBinaryReader () {
        super("TC_0012_READER_FILEBINARYREADER");
    }

    @Override
    public void executeTestCase () {
        if (this.createTestFile()) {
            try {
                FileBinaryReader fileBinaryReader = new FileBinaryReader(0x20);
                fileBinaryReader.open(PathUtils.getTestSuiteFolderPath("\\test.bin"));
                fileBinaryReader.setByteOrder(ByteOrder.BIG_ENDIAN);

                Int8 int8 = fileBinaryReader.readInt8();

                if (int8.getValue() != -23) {
                    this.failed("FAILED_TO_READ_INT8");
                    return;
                }

                fileBinaryReader.skipOffsetPosition(0x4);

                Int16 int16 = fileBinaryReader.readInt16();

                if (int16.getValue() != -24312) {
                    this.failed("FAILED_TO_READ_INT16");
                    return;
                }

                fileBinaryReader.skipOffsetPosition(0x3);

                Int32 int32 = fileBinaryReader.readInt32();

                if (int32.getValue() != 99974411) {
                    this.failed("FAILED_TO_READ_INT32");
                    return;
                }

                Int64 int64 = fileBinaryReader.readInt64();

                if (!(int64.getValue().equals(new BigInteger("-300120021")))) {
                    this.failed("FAILED_TO_READ_INT64");
                    return;
                }

                UInt8 uInt8 = fileBinaryReader.readUInt8();

                if (uInt8.getValue() != 69) {
                    this.failed("FAILED_TO_READ_UINT8");
                    return;
                }

                UInt16 uInt16 = fileBinaryReader.readUInt16();

                if (uInt16.getValue() != 420) {
                    this.failed("FAILED_TO_READ_UINT16");
                    return;
                }

                UInt32 uInt32 = fileBinaryReader.readUInt32();

                if (uInt32.getValue() != 945125151) {
                    this.failed("FAILED_TO_READ_UINT32");
                    return;
                }

                UInt64 uInt64 = fileBinaryReader.readUInt64();

                if (!(uInt64.getValue().equals(new BigInteger("352523542352")))) {
                    this.failed("FAILED_TO_READ_UINT64");
                    return;
                }

                float binaryFloat = fileBinaryReader.readFloat();

                if (binaryFloat != 85411f) {
                    this.failed("FAILED_TO_READ_FLOAT");
                    return;
                }

                double binaryDouble = fileBinaryReader.readDouble();

                if (binaryDouble != 24255.235d) {
                    this.failed("FAILED_TO_READ_DOUBLE");
                    return;
                }

                String binaryString = fileBinaryReader.readString(18);

                if (!(binaryString.equals("You are beautiful!"))) {
                    this.failed("FAILED_TO_READ_STRING");
                    return;
                }

                byte[] binaryBytes = fileBinaryReader.readBytes(1);

                if (binaryBytes[0] != 0x02) {
                    this.failed("FAILED_TO_READ_BYTES");
                    return;
                }

                if (fileBinaryReader.getFileOffsetPosition() != 0x44) {
                    this.failed("INVALID_OFFSET_POSITION");
                    return;
                }

                if (fileBinaryReader.getFileLength() != 0x4B) {
                    this.failed("INVALID_FILE_SIZE");
                    return;
                }

                fileBinaryReader.close();

                Files.deleteIfExists(PathUtils.getTestSuiteFolderPath("\\test.bin"));

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
                this.failed("RANGE_OUT_OF_BOUNDS");
                rangeOutOfBoundsException.printStackTrace();
            }
        }
    }

    private boolean createTestFile () {
        BinaryWriter binaryWriter = new BinaryWriter(ByteOrder.BIG_ENDIAN);
        binaryWriter.enableChunkPadding(0x5, (byte) 0xAE);

        if (!(binaryWriter.isChunkPaddingEnabled())) {
            this.failed("CHUNK_PADDING_IS_NOT_ENABLED");
            return false;
        }

        if (binaryWriter.getChunkSize() != 0x5) {
            this.failed("CHUNK_SIZE_IS_INVALID");
            return false;
        }

        Int8 int8;
        Int16 int16;
        Int32 int32;
        Int64 int64;
        UInt8 uInt8;
        UInt16 uInt16;
        UInt32 uInt32;
        UInt64 uInt64;
        float binaryFloat = 85411f;
        double binaryDouble = 24255.235d;
        String binaryString = "You are beautiful!";
        byte[] binaryBytes;

        try {
            int8 = new Int8("-23");
            int16 = new Int16(-24312);
            int32 = new Int32(99974411);
            int64 = new Int64("-300120021");
            uInt8 = new UInt8("69");
            uInt16 = new UInt16(420);
            uInt32 = new UInt32(945125151);
            uInt64 = new UInt64("352523542352");

            binaryBytes = new byte[1];
            binaryBytes[0] = 0x02;
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STRUCTURES_RANGE_OUT_OF_BOUNDS");
            rangeOutOfBoundsException.printStackTrace();
            return false;
        }

        try {
            binaryWriter.open(PathUtils.getTestSuiteFolderPath("\\test.bin"), true);

            if (binaryWriter.isClosed()) {
                this.failed("BINARY_WRITER_CLOSED");
                return false;
            }

            binaryWriter.write(int8);
            binaryWriter.write(int16);
            binaryWriter.disableChunkPadding();
            binaryWriter.write(int32);
            binaryWriter.write(int64);
            binaryWriter.write(uInt8);
            binaryWriter.write(uInt16);
            binaryWriter.write(uInt32);
            binaryWriter.write(uInt64);
            binaryWriter.write(binaryFloat);
            binaryWriter.write(binaryDouble);
            binaryWriter.write(binaryString, StandardCharsets.UTF_8);
            binaryWriter.write(binaryBytes);
            binaryWriter.fill((byte) 0x0, 0x7);

            long fileLength = binaryWriter.getFileLength();
            binaryWriter.close();

            if (fileLength != 0x4B) {
                this.failed("FILE_LENGTH_INVALID");
                return false;
            }

            byte[] fileBytes = Files.readAllBytes(PathUtils.getTestSuiteFolderPath("\\test.bin"));

            String hexString = HEXUtils.convertBytesToHEXString(fileBytes);

            if (!(hexString.equals("E9AEAEAEAEA108AEAEAE05F57D0BFFFFFFFFEE1C882B4501A43855771F00000052140A5F5047A6D18040D7AFCF0A3D70A4596F75206172652062656175746966756C210200000000000000"))) {
                this.failed("HEX_CHECK");
                return false;
            }

            return true;
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

        return false;
    }
}