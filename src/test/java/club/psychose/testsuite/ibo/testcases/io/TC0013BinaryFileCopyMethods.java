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

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.utils.PathUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public final class TC0013BinaryFileCopyMethods {
    @Test
    public void executeTestCase () {
        Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");
        Path fileToCopyTest1Path = PathUtils.getTestSuiteFolderPath("\\test_2.bin");
        Path fileToCopyTest2Path = PathUtils.getTestSuiteFolderPath("\\test_3.bin");
        Path fileToCopyTest3Path = PathUtils.getTestSuiteFolderPath("\\test_4.bin");
        Path fileToCopyTest4Path = PathUtils.getTestSuiteFolderPath("\\test_5.bin");

        try {
            this.createTestFile(filePath);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        BinaryFile testBinaryFile;
        try {
            testBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            testBinaryFile.open(filePath, 0x0, FileMode.READ);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.copy(ByteOrder.BIG_ENDIAN, fileToCopyTest1Path, true);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.copy(ByteOrder.BIG_ENDIAN, fileToCopyTest2Path, false);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            Files.deleteIfExists(fileToCopyTest3Path);
            Files.createFile(fileToCopyTest3Path);

            BinaryFile test3ChunkBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            test3ChunkBinaryFile.open(fileToCopyTest3Path, 0x0, FileMode.READ_AND_WRITE);
            test3ChunkBinaryFile.setChunkUsage(true, 0x2);
            testBinaryFile.copy(test3ChunkBinaryFile, false);
            test3ChunkBinaryFile.close();
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            Files.deleteIfExists(fileToCopyTest4Path);
            Files.createFile(fileToCopyTest4Path);

            BinaryFile test4ChunkBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            test4ChunkBinaryFile.open(fileToCopyTest4Path, 0x0, FileMode.WRITE);
            testBinaryFile.copy(test4ChunkBinaryFile, true);
            test4ChunkBinaryFile.close();
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        // Checking the files.
        try {
            BinaryFile test1BinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            BinaryFile test2BinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            BinaryFile test3BinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            BinaryFile test4BinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);

            test1BinaryFile.open(fileToCopyTest1Path, 0x0, FileMode.READ);
            test2BinaryFile.open(fileToCopyTest2Path, 0x0, FileMode.READ);
            test3BinaryFile.open(fileToCopyTest3Path, 0x0, FileMode.READ);
            test4BinaryFile.open(fileToCopyTest4Path, 0x0, FileMode.READ);

            assertTrue(this.hasSampleData(test1BinaryFile));
            assertTrue(this.hasSampleData(test2BinaryFile));
            assertTrue(this.hasSampleData(test3BinaryFile));
            assertTrue(this.hasSampleData(test4BinaryFile));

            test1BinaryFile.close();
            test2BinaryFile.close();
            test3BinaryFile.close();
            test4BinaryFile.close();
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.close();
            Files.deleteIfExists(filePath);
            Files.deleteIfExists(fileToCopyTest1Path);
            Files.deleteIfExists(fileToCopyTest2Path);
            Files.deleteIfExists(fileToCopyTest3Path);
            Files.deleteIfExists(fileToCopyTest4Path);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
        }
    }

    private void createTestFile (Path filePath) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        // Setup part.
        Files.deleteIfExists(filePath);

        if (!(Files.exists(PathUtils.getTestSuiteFolderPath(null))))
            Files.createDirectories(PathUtils.getTestSuiteFolderPath(null));

        Files.createFile(filePath);

        // Writing sample data.
        BinaryFile testBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
        testBinaryFile.open(filePath, 0x0, FileMode.WRITE);
        testBinaryFile.write(new UInt32(0x0));
        testBinaryFile.write(new UInt32(0x1));
        testBinaryFile.write(new UInt32(0x2));
        testBinaryFile.write(new UInt32(0x3));
        testBinaryFile.write(new UInt32(0x4));
        testBinaryFile.write(new UInt32(0x5));
        testBinaryFile.write(new UInt32(0x6));
        testBinaryFile.write(new UInt32(0x7));
        testBinaryFile.write(new UInt32(0x8));
        testBinaryFile.write(new UInt32(0x9));
        testBinaryFile.close();
    }

    public boolean hasSampleData (BinaryFile testBinaryFile) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        boolean valid = true;

        if (testBinaryFile.getFileLength() < 10)
            return false;

        for (int dataIndex = 0; dataIndex < 10; dataIndex++) {
            if (testBinaryFile.readUInt32().getValue() != dataIndex) {
                valid = false;
                break;
            }
        }

        return valid;
    }
}