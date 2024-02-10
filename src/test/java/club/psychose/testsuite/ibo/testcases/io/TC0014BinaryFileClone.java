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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class TC0014BinaryFileClone {
    @Test
    public void executeTestCase () {
        Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");

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
            testBinaryFile.open(filePath, 0x4, FileMode.READ);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        BinaryFile clonedBinaryFile;
        try {
            clonedBinaryFile = testBinaryFile.clone();
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            clonedBinaryFile.setOffsetPosition(0x0);
            assertEquals(testBinaryFile.getFileOffsetPosition(), clonedBinaryFile.getFileOffsetPosition());

            assertEquals(clonedBinaryFile.readUInt32().getValue(), 0x0);
            assertEquals(testBinaryFile.readUInt32().getValue(), 0x1);

            assertEquals(testBinaryFile.getFileOffsetPosition(), clonedBinaryFile.getFileOffsetPosition());
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.close();
            clonedBinaryFile.close();
            Files.deleteIfExists(filePath);
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
}