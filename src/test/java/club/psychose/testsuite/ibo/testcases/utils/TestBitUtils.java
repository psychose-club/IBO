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

package club.psychose.testsuite.ibo.testcases.utils;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.library.ibo.utils.BitUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public final class TestBitUtils {
    @Test
    public void executeTestCase () {
        AtomicReference<Int8> int8 = new AtomicReference<>();
        AtomicReference<UInt8> uInt8 = new AtomicReference<>();
        AtomicReference<Int16> int16 = new AtomicReference<>();
        AtomicReference<UInt16> uInt16 = new AtomicReference<>();
        AtomicReference<Int24> int24 = new AtomicReference<>();
        AtomicReference<UInt24> uInt24 = new AtomicReference<>();
        AtomicReference<Int32> int32 = new AtomicReference<>();
        AtomicReference<UInt32> uInt32 = new AtomicReference<>();
        AtomicReference<Int64> int64 = new AtomicReference<>();
        AtomicReference<UInt64> uInt64 = new AtomicReference<>();
        byte testByte;
        short testShort;
        int testInt;
        long testLong;

        // Initializing all required variables.
        assertDoesNotThrow(() -> int8.set(new Int8(127)));
        assertDoesNotThrow(() -> uInt8.set(new UInt8(10)));
        assertDoesNotThrow(() -> int16.set(new Int16(160)));
        assertDoesNotThrow(() -> uInt16.set(new UInt16(34)));
        assertDoesNotThrow(() -> int24.set(new Int24(12)));
        assertDoesNotThrow(() -> uInt24.set(new UInt24(555)));
        assertDoesNotThrow(() -> int32.set(new Int32(4000)));
        assertDoesNotThrow(() -> uInt32.set(new UInt32(9000)));
        assertDoesNotThrow(() -> int64.set(new Int64(-180)));
        assertDoesNotThrow(() -> uInt64.set(new UInt64(69999)));

        testByte = (byte) 12;
        testShort = (short) 4154;
        testInt = 89494;
        testLong = -623L;

        // Out-of-bounds check.
        assertThrows(RangeOutOfBoundsException.class, () -> BitUtils.extractBitsViaIndex(int8.get(), 0, -1));
        assertThrows(RangeOutOfBoundsException.class, () -> BitUtils.extractBitsViaIndex(int8.get(), -1, 0));
        assertThrows(RangeOutOfBoundsException.class, () -> BitUtils.extractBitsViaIndex(int8.get(), 0, Int8.getBitLength() + 1));

        try {
            long int8BitResult = BitUtils.extractBitsViaIndex(int8.get(), 0, 2);

            // The result should be 0000 0111 (7)
            assertEquals(int8BitResult, 7);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long uInt8BitResult = BitUtils.extractBitsViaIndex(uInt8.get(), 0, 0);

            // The result should be 0000 0000 (0)
            assertEquals(uInt8BitResult, 0);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long int16BitResult = BitUtils.extractBitsViaIndex(int16.get(), 0, 1);

            // The result should be 0000 0000 0000 0000 (0)
            assertEquals(int16BitResult, 0);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long uInt16BitResult = BitUtils.extractBitsViaIndex(uInt16.get(), 0, 4);

            // The result should be 0000 0000 0000 0010 (2)
            assertEquals(uInt16BitResult, 2);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long int24BitResult = BitUtils.extractBitsViaIndex(int24.get(), 0, 8);

            // The result should be 0000 0000 0000 0000 0000 1100 (12)
            assertEquals(int24BitResult, 12);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long uInt24BitResult = BitUtils.extractBitsViaIndex(uInt24.get(), 0, 0);

            // The result should be 0000 0000 0000 0000 0000 0001 (1)
            assertEquals(uInt24BitResult, 1);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long int32BitResult = BitUtils.extractBitsViaIndex(int32.get(), 0, 1);

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 (0)
            assertEquals(int32BitResult, 0);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long uInt32BitResult = BitUtils.extractBitsViaIndex(uInt32.get(), 0, 8);

            // The result should be 0000 0000 0000 0000 0000 0001 0010 1000 (296)
            assertEquals(uInt32BitResult, 296);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long int64BitResult = -(BitUtils.extractBitsViaIndex(int64.get(), 0, 2));

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0100 (4)
            assertEquals(int64BitResult, -4);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long uInt64BitResult = BitUtils.extractBitsViaIndex(uInt64.get(), 0, 9);

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001 0110 1111 (367)
            assertEquals(uInt64BitResult, 367);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long byteBitResult = BitUtils.extractBitsViaIndex(testByte, 0, 5);

            // The result should be 0000 1100 (12)
            assertEquals(byteBitResult, 12);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long shortBitResult = BitUtils.extractBitsViaIndex(testShort, 0, 8);

            // The result should be 0000 0000 0011 1010 (58)
            assertEquals(shortBitResult, 58);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long intBitResult = BitUtils.extractBitsViaIndex(testInt, 0, 6);

            // The result should be 0000 0000 0001 0110 (22)
            assertEquals(intBitResult, 22);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
            return;
        }

        try {
            long longBitResult = -(BitUtils.extractBitsViaIndex(testLong, 0, 0));

            // The result should be 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001 (1)
            assertEquals(longBitResult, -1);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            fail("An exception occurred while executing the testcase!");
        }
    }
}