/*
 * Copyright (c) 2023-2024, psychose.club
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

package club.psychose.testsuite.ibo.testcases.io;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.utils.PathUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public final class TestBinaryFile {
    @Test
    public void executeTestCase () {
        try {
            // Setup part.
            Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");
            Files.deleteIfExists(filePath);

            if (!(Files.exists(PathUtils.getTestSuiteFolderPath(null))))
                Files.createDirectories(PathUtils.getTestSuiteFolderPath(null));

            Files.createFile(filePath);

            // Test part.
            BinaryFile binaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);

            // Check if nothing will be thrown when close is called without an open called before.
            binaryFile.close();

            // Try to access a method without that the file is opened.
            assertThrows(ClosedException.class, binaryFile::getRemainingFileBytes);

            // Open + close.
            binaryFile.open(filePath, 0x0);
            binaryFile.close();

            // Check if range is out of bounds when a position is called that didn't exist.
            assertThrows(RangeOutOfBoundsException.class, () -> binaryFile.open(filePath, 0x02));

            // Check if invalid file mode exception is thrown when we try to access write methods in the read mode.
            binaryFile.open(filePath, 0x0, FileMode.READ);
            assertThrows(InvalidFileModeException.class, () -> binaryFile.write(0x0));
            binaryFile.close();

            // Check if invalid file mode exception is thrown when we try to access read methods in the written mode.
            binaryFile.open(filePath, 0x0, FileMode.WRITE);
            assertThrows(InvalidFileModeException.class, () -> binaryFile.readBytes(1));
            binaryFile.close();

            // Check if an io exception is thrown when we try to access chunk methods without enabling the chunk mode.
            binaryFile.open(filePath, 0x0, FileMode.READ_AND_WRITE);
            assertThrows(IOException.class, binaryFile::updateChunk);

            // Check if every write method works without any issues.
            {
                byte[] bytes = new byte[2];
                bytes[0] = 0x9;
                bytes[1] = 0x54;

                binaryFile.write(bytes);
            }

            binaryFile.write(new Int8(0));
            binaryFile.write(new UInt8(1));
            binaryFile.write(new Int16(2));
            binaryFile.write(new UInt16(3));
            binaryFile.write(new Int32(4));
            binaryFile.write(new UInt32(5));
            binaryFile.write(new Int64(6));
            binaryFile.write(new UInt64(7));
            binaryFile.write((byte) 0x0);
            binaryFile.write((float) 3.4);
            binaryFile.write(4.5);
            binaryFile.write("LoveeYou<3<3<3");
            binaryFile.write(new Int24(8));
            binaryFile.write(new UInt24(9));

            assertEquals(0x41, binaryFile.getFileOffsetPosition());

            // Enabling padding
            binaryFile.enablePadding(0x10, (byte) 0xFF);
            binaryFile.write((byte) 0x43);
            assertEquals(0x51, binaryFile.getFileOffsetPosition());

            binaryFile.fill((byte) 0x2A, 2);
            assertEquals(0x61, binaryFile.getFileOffsetPosition());

            binaryFile.fillWithoutPadding((byte) 0x31, 0x05);
            assertEquals(0x66, binaryFile.getFileOffsetPosition());

            binaryFile.disablePadding();
            binaryFile.write(new Int8(12));
            assertEquals(0x67, binaryFile.getFileOffsetPosition());

            // Check if every read method works without any issues.
            binaryFile.setOffsetPosition(0x1);
            assertEquals(0x54, binaryFile.readBytes(1)[0]);

            binaryFile.setOffsetPosition(0x0);
            assertEquals(0x9, binaryFile.readBytes(1)[0]);

            binaryFile.skipOffsetPosition(1);
            assertEquals((short) 0x0, binaryFile.readInt8().getValue());

            binaryFile.setOffsetPosition(0x0);
            {
                byte[] bytes = binaryFile.readBytes(2);
                assertEquals(0x9, bytes[0]);
                assertEquals(0x54, bytes[1]);
            }

            assertEquals((short) 0x0, binaryFile.readInt8().getValue());
            assertEquals((short) 0x1, binaryFile.readUInt8().getValue());
            assertEquals(0x2, binaryFile.readInt16().getValue());
            assertEquals(0x3, binaryFile.readUInt16().getValue());
            assertEquals(0x4, binaryFile.readInt32().getValue());

            binaryFile.setStayOnOffsetPosition(true);
            assertEquals(0x5, binaryFile.readUInt32().getValue());
            assertEquals(0x5, binaryFile.readUInt32().getValue());
            assertEquals(0x5, binaryFile.readUInt32().getValue());
            binaryFile.setStayOnOffsetPosition(false);

            assertEquals(0x5, binaryFile.readUInt32().getValue());
            assertEquals(0x6, binaryFile.readInt64().getValue().intValue());

            binaryFile.setStayOnOffsetPosition(true);
            assertEquals(0x7, binaryFile.readUInt64().getValue().intValue());
            assertEquals(0x7, binaryFile.readUInt64().getValue().intValue());
            assertEquals(0x7, binaryFile.readUInt64().getValue().intValue());
            binaryFile.setStayOnOffsetPosition(false);

            assertEquals(0x7, binaryFile.readUInt64().getValue().intValue());
            assertEquals(0x0, binaryFile.readInt8().getValue().intValue()); // Byte = Int8
            assertEquals(3.4f, binaryFile.readFloat());
            assertEquals(4.5, binaryFile.readDouble());
            assertEquals("LoveeYou<3<3<3", binaryFile.readString(14));
            assertEquals(0x8, binaryFile.readInt24().getValue());
            assertEquals(0x9, binaryFile.readUInt24().getValue());
            assertEquals(0x41, binaryFile.getFileOffsetPosition());

            // Check if an io exception is thrown when we try to enable the chunk mode without setting a specified chunk length.
            binaryFile.setOffsetPosition(0x0);
            assertThrows(IOException.class, () -> binaryFile.setChunkUsage(true));

            binaryFile.setChunkLength(0x10);
            binaryFile.setChunkUsage(true);

            assertEquals(0x9, binaryFile.readBytes(1)[0]);

            binaryFile.setOffsetPosition(0x8);
            assertEquals(0x4, binaryFile.readInt32().getValue()); // Reading bytes from two chunks while reading the value.

            byte[] sequence1 = new byte[3];
            sequence1[0] = (byte) 0xFF;
            sequence1[1] = (byte) 0xFF;
            sequence1[2] = (byte) 0xFF;

            long sequence1Offset = binaryFile.searchNextByteSequence(sequence1);
            assertEquals(0x42, sequence1Offset);

            binaryFile.disablePadding();
            String sequence2 = "2AFF";
            long sequence2Offset = binaryFile.searchNextHEXValue(sequence2);

            assertEquals(0x52, sequence2Offset);
            assertTrue(binaryFile.isChunkUsageEnabled());

            // Cleanup part.
            binaryFile.setChunkUsage(false);
            binaryFile.close();
            Files.deleteIfExists(filePath);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
        }
    }
}