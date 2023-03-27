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
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;

public final class TC0015ReaderHEXValueSearchAllChunksFileBinaryReader extends Test {
    public TC0015ReaderHEXValueSearchAllChunksFileBinaryReader () {
        super("TC_0015_READER_HEXVALUESEARCH_ALLCHUNKS_FILEBINARYREADER");
    }

    @Override
    public void executeTestCase() {
        if (this.createTestFile()) {
            try {
                FileBinaryReader fileBinaryReader = new FileBinaryReader(0x06);
                fileBinaryReader.open(PathUtils.getTestSuiteFolderPath("\\test.bin"));
                fileBinaryReader.setByteOrder(ByteOrder.BIG_ENDIAN);

                long firstOffsetPosition = fileBinaryReader.searchFirstHEXValueInAllChunks("ABCDEF", (long) 0xA);

                if (firstOffsetPosition != 0x16) {
                    fileBinaryReader.close();
                    this.failed("FIRST_OFFSET_POSITION_INVALID");
                    return;
                }

                long secondOffsetPosition = fileBinaryReader.searchFirstHEXValueInAllChunks("05E0", 0, 5);

                if (secondOffsetPosition != 0xA) {
                    fileBinaryReader.close();
                    this.failed("SECOND_OFFSET_POSITION_INVALID");
                    return;
                }

                long thirdOffsetPosition = fileBinaryReader.searchFirstHEXValueInAllChunks("54657374", 1);

                if (thirdOffsetPosition != 0xC) {
                    fileBinaryReader.close();
                    this.failed("THIRD_OFFSET_POSITION_INVALID");
                    return;
                }

                long fourthOffsetPosition = fileBinaryReader.searchFirstHEXValueInAllChunks("1515");

                if (fourthOffsetPosition != 0x1E) {
                    fileBinaryReader.close();
                    this.failed("FOURTH_OFFSET_POSITION_INVALID");
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

        try {
            binaryWriter.open(PathUtils.getTestSuiteFolderPath("\\test.bin"), true);

            if (binaryWriter.isClosed()) {
                this.failed("BINARY_WRITER_CLOSED");
                return false;
            }

            binaryWriter.write((byte) 0x0);
            binaryWriter.write((byte) 0x0);
            binaryWriter.write((byte) 0x0);
            binaryWriter.write((byte) 0xAF);
            binaryWriter.write((byte) 0xDA);
            binaryWriter.write((byte) 0x41);
            binaryWriter.write((byte) 0x51);
            binaryWriter.write((byte) 0x52);
            binaryWriter.write((byte) 0x01);
            binaryWriter.write((byte) 0x51);
            binaryWriter.write((byte) 0x05);
            binaryWriter.write((byte) 0xE0);
            binaryWriter.write((byte) 0x54);
            binaryWriter.write((byte) 0x65);
            binaryWriter.write((byte) 0x73);
            binaryWriter.write((byte) 0x74);
            binaryWriter.write((byte) 0xAD);
            binaryWriter.write((byte) 0xFA);
            binaryWriter.write((byte) 0xFF);
            binaryWriter.write((byte) 0xDE);
            binaryWriter.write((byte) 0xDF);
            binaryWriter.write((byte) 0xCA);
            binaryWriter.write((byte) 0xAB);
            binaryWriter.write((byte) 0xCD);
            binaryWriter.write((byte) 0xEF);
            binaryWriter.write((byte) 0x23);
            binaryWriter.write((byte) 0x41);
            binaryWriter.write((byte) 0x24);
            binaryWriter.write((byte) 0x21);
            binaryWriter.write((byte) 0x42);
            binaryWriter.write((byte) 0x15);
            binaryWriter.write((byte) 0x15);

            long fileLength = binaryWriter.getFileLength();
            binaryWriter.close();

            if (fileLength != 0x20) {
                this.failed("FILE_LENGTH_INVALID");
                return false;
            }
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            this.failed("IO_EXCEPTION");
            ioException.printStackTrace();
        }

        return true;
    }
}