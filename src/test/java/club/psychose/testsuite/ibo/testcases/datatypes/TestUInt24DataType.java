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

package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt24;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUInt24DataType {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt24(UInt24.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt24(UInt24.getMaximumValue() + 1));

        // Storing and fetching values.
        UInt24 uInt24 = new UInt24(1337);
        UInt24 secondUInt16 = new UInt24(2222);

        int storedValue = uInt24.getValue();
        assertEquals(1337, storedValue);
        storedValue = 1231;
        assertEquals(1231, storedValue);

        uInt24.setValue(2222);
        assertEquals(2222, uInt24.getValue());
        assertEquals(uInt24.getValue(), secondUInt16.getValue());
        assertEquals("2222", uInt24.toString());

        String hexString = uInt24.getAsHEXString(HEXFormat.LOWERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals("ae0800", hexString);

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = new byte[3];

        boolean useBigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        if (useBigEndian) {
            bytesWithoutSetByteOrder[1] = 0x50;
            bytesWithoutSetByteOrder[2] = 0x57;
        } else {
            bytesWithoutSetByteOrder[0] = 0x57;
            bytesWithoutSetByteOrder[1] = 0x50;
        }

        assertEquals(20567, new UInt24(bytesWithoutSetByteOrder).getValue());

        // We will reverse the check now to check both ByteOrders.
        useBigEndian = !(useBigEndian);

        byte[] bytesWithByteOrder = new byte[9];

        if (useBigEndian) {
            bytesWithByteOrder[1] = 0x26;
            bytesWithByteOrder[2] = (byte) 0xF6;
        } else {
            bytesWithByteOrder[0] = (byte) 0xF6;
            bytesWithByteOrder[1] = 0x26;
        }

        assertEquals(9974, new UInt24(bytesWithByteOrder, (useBigEndian) ? (ByteOrder.BIG_ENDIAN) : (ByteOrder.LITTLE_ENDIAN)).getValue());
        assertEquals(50, new UInt24((byte) 50).getValue());
        assertEquals(1541, new UInt24((short) 1541).getValue());
        assertEquals(16777215, new UInt24((long) 16777215).getValue());
        assertEquals(2145, new UInt24(2145f).getValue());
        assertEquals(0, new UInt24(0.45).getValue());
        assertEquals(99654, new UInt24(BigInteger.valueOf(99654)).getValue());
        assertEquals(0, new UInt24("0").getValue());
    }
}