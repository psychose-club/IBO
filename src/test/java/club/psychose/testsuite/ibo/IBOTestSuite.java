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

package club.psychose.testsuite.ibo;

import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.testcases.reader.*;
import club.psychose.testsuite.ibo.testcases.structures.*;
import club.psychose.testsuite.ibo.testcases.writer.TC0009WriterBinaryWriterNoChunkPadding;
import club.psychose.testsuite.ibo.testcases.writer.TC0010WriterBinaryWriterChunkPadding;
import club.psychose.testsuite.ibo.utils.logging.ConsoleLogger;

import java.util.ArrayList;

public final class IBOTestSuite {
    private static final ArrayList<Test> testCasesArrayList = new ArrayList<>();
    private static short succeededTestCases = 0;

    public static void main(String[] arguments) {
        registerTestCases();
        runTests();
    }

    private static void registerTestCases() {
        testCasesArrayList.add(new TC0001StructureInt8()); // Int8
        testCasesArrayList.add(new TC0002StructureUInt8()); // UInt8
        testCasesArrayList.add(new TC0003StructureInt16()); // Int16
        testCasesArrayList.add(new TC0004StructureUInt16()); // UInt16
        testCasesArrayList.add(new TC0005StructureInt32()); // Int32
        testCasesArrayList.add(new TC0006StructureUInt32()); // UInt32
        testCasesArrayList.add(new TC0007StructureInt64()); // Int64
        testCasesArrayList.add(new TC0008StructureUInt64()); // UInt64
        testCasesArrayList.add(new TC0009WriterBinaryWriterNoChunkPadding()); // BinaryWriter without chunk padding.
        testCasesArrayList.add(new TC0010WriterBinaryWriterChunkPadding()); // BinaryWriter with chunk padding.
        testCasesArrayList.add(new TC0011ReaderMemoryBinaryReader()); // MemoryBinaryReader
        testCasesArrayList.add(new TC0012ReaderFileBinaryReader()); // FileBinaryReader
        testCasesArrayList.add(new TC0013ReaderPaddingRemoval()); // Removing the padding from bytes.
        testCasesArrayList.add(new TC0014ReaderHEXValueSearchMemoryBinaryReader()); // Searches for a HEX value in the memory.
        testCasesArrayList.add(new TC0015ReaderHEXValueSearchAllChunksFileBinaryReader()); // Searches for a HEX value in a file through all chunks.
        testCasesArrayList.add(new TC0016ReaderHEXValueSearchInOneChunkFileBinaryReader()); // Searches for a HEX value in a file through one specified chunk.
        testCasesArrayList.add(new TC0017ReaderStayOnOffsetPosition()); // Checks if the readers are staying on the current offset when it should be updated.
    }

    private static void runTests () {
        testCasesArrayList.forEach((test) -> {
            ConsoleLogger.printConsole("Running \"" + test.getTestCaseName() + "\"...");

            test.executeTestCase();
            if (test.isPassed())
                succeededTestCases ++;

            ConsoleLogger.printEmptyLine();
        });

        if (testCasesArrayList.size() != 0) {
            if (testCasesArrayList.size() == succeededTestCases) {
                ConsoleLogger.printConsole("[SUCCESS] All " + succeededTestCases + " TestCases passed successfully!");
            } else {
                ConsoleLogger.printConsole("[FAILED] Only " + succeededTestCases + " out of " + testCasesArrayList.size() + " TestCases passed successfully!");
            }
        }
    }
}