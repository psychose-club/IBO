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

package club.psychose.testsuite.ibo.testcases.datatypes;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TC0008DatatypeUInt64 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt64(new BigInteger(UInt64.getMinimumValue().toString()).subtract(BigInteger.valueOf(1))));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt64(new BigInteger(UInt64.getMaximumValue().toString()).add(BigInteger.valueOf(1))));

        // Storing and fetching values.
        UInt64 uInt64 = new UInt64(UInt64.getMaximumValue());
        UInt64 secondUInt64 = new UInt64(UInt64.getMinimumValue());

        BigInteger storedValue = uInt64.getValue();
        assertEquals(storedValue, UInt64.getMaximumValue());
        storedValue = BigInteger.valueOf(41541555);
        assertEquals(storedValue, BigInteger.valueOf(41541555));

        uInt64.setValue(UInt64.getMinimumValue());
        assertEquals(uInt64.getValue(), UInt64.getMinimumValue());
        assertEquals(uInt64.getValue(), secondUInt64.getValue());
        assertEquals(uInt64.toString(), UInt64.getMinimumValue().toString());

        String hexString = uInt64.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals(hexString, "0000000000000000");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(249664).array();
        assertEquals(new UInt64(bytesWithoutSetByteOrder).getValue().longValue(), 249664);

        byte[] bytesWithByteOrder = new byte[123];
        bytesWithByteOrder[0] = 0x11;
        bytesWithByteOrder[1] = 0x18;
        bytesWithByteOrder[2] = 0x00;
        bytesWithByteOrder[3] = 0x00;
        bytesWithByteOrder[4] = 0x00;
        bytesWithByteOrder[5] = 0x00;
        bytesWithByteOrder[6] = 0x00;
        bytesWithByteOrder[7] = 0x00;

        assertEquals(new UInt64(bytesWithByteOrder, ByteOrder.LITTLE_ENDIAN).getValue().longValue(), 6161);
        assertEquals(new UInt64((byte) 13).getValue().longValue(), 13);
        assertEquals(new UInt64((short) 21).getValue().longValue(), 21);
        assertEquals(new UInt64((long) 2222).getValue().longValue(), 2222);
        assertEquals(new UInt64(0f).getValue().longValue(), 0);
        assertEquals(new UInt64(0.69).getValue().longValue(), 0);
        assertEquals(new UInt64(BigInteger.valueOf(1234)).getValue().longValue(), 1234);
        assertEquals(new UInt64("42").getValue().longValue(), 42);
    }
}