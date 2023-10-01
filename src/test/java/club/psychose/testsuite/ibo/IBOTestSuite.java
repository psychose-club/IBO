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
import club.psychose.testsuite.ibo.testcases.datatypes.*;
import club.psychose.testsuite.ibo.testcases.io.TC0009BinaryFile;
import club.psychose.testsuite.ibo.utils.logging.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;

public final class IBOTestSuite {
    private static final List<Test> testCasesArrayList = new ArrayList<>();
    private static short succeededTestCases = 0;

    public static void main (String[] arguments) {
        registerTestCases();
        runTests();
    }

    private static void registerTestCases () {
        // Data types.
        testCasesArrayList.add(new TC0001DatatypeInt8()); // Int8
        testCasesArrayList.add(new TC0002DatatypeUInt8()); // UInt8
        testCasesArrayList.add(new TC0003DatatypeInt16()); // Int16
        testCasesArrayList.add(new TC0004DatatypeUInt16()); // UInt16
        testCasesArrayList.add(new TC0005DatatypeInt32()); // Int32
        testCasesArrayList.add(new TC0006DatatypeUInt32()); // UInt32
        testCasesArrayList.add(new TC0007DatatypeInt64()); // Int64
        testCasesArrayList.add(new TC0008DatatypeUInt64()); // UInt64
        testCasesArrayList.add(new TC0010DatatypeInt24()); // Int24
        testCasesArrayList.add(new TC0011DatatypeUInt24()); // UInt24

        // IO
        testCasesArrayList.add(new TC0009BinaryFile()); // BinaryFile
    }

    private static void runTests () {
        testCasesArrayList.forEach((test) -> {
            ConsoleLogger.printConsole("[RUNNING] \"" + test.getTestCaseName() + "\"...");

            test.executeTestCase();
            if (test.isPassed())
                succeededTestCases++;

            ConsoleLogger.printEmptyLine();
        });

        if (!(testCasesArrayList.isEmpty())) {
            if (testCasesArrayList.size() == succeededTestCases) {
                ConsoleLogger.printConsole("[SUCCESS] All " + succeededTestCases + " TestCases passed successfully!");
            } else {
                ConsoleLogger.printConsole("[FAILED] Only " + succeededTestCases + " out of " + testCasesArrayList.size() + " TestCases passed successfully!");
            }
        }
    }
}