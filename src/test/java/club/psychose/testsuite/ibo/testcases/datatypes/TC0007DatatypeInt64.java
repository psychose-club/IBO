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

import club.psychose.library.ibo.core.datatypes.types.signed.Int64;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TC0007DatatypeInt64 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new Int64(new BigInteger(Int64.getMinimumValue().toString()).subtract(BigInteger.valueOf(1))));
        assertThrows(RangeOutOfBoundsException.class, () -> new Int64(new BigInteger(Int64.getMaximumValue().toString()).add(BigInteger.valueOf(1))));

        // Storing and fetching values.
        Int64 int64 = new Int64(969699);
        Int64 secondInt64 = new Int64(545252121212L);

        BigInteger storedValue = int64.getValue();
        assertEquals(storedValue, BigInteger.valueOf(969699));
        storedValue = new BigInteger("24242455");
        assertEquals(storedValue, BigInteger.valueOf(24242455));

        int64.setValue(545252121212L);
        assertEquals(int64.getValue().longValue(), 545252121212L);
        assertEquals(int64.getValue(), secondInt64.getValue());
        assertEquals(int64.toString(), "545252121212");

        String hexString = int64.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);
        assertEquals(hexString, "0000007EF38F1A7C");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(-24124).array();
        assertEquals(new Int64(bytesWithoutSetByteOrder).getValue().longValue(), -24124);

        byte[] bytesWithByteOrder = new byte[123];
        bytesWithByteOrder[0] = 0x00;
        bytesWithByteOrder[1] = 0x00;
        bytesWithByteOrder[2] = 0x00;
        bytesWithByteOrder[3] = 0x00;
        bytesWithByteOrder[4] = 0x00;
        bytesWithByteOrder[5] = 0x00;
        bytesWithByteOrder[6] = (byte) 0xB0;
        bytesWithByteOrder[7] = (byte) 0xBD;

        assertEquals(new Int64(bytesWithByteOrder, ByteOrder.BIG_ENDIAN).getValue().longValue(), 45245);
        assertEquals(new Int64((byte) 111).getValue().longValue(), 111);
        assertEquals(new Int64((short) 213).getValue().longValue(), 213);
        assertEquals(new Int64((long) 123).getValue().longValue(), 123);
        assertEquals(new Int64(2.2512f).getValue().longValue(), (int) 2f);
        assertEquals(new Int64(12321.3).getValue().longValue(), 12321);
        assertEquals(new Int64(BigInteger.valueOf(1231341535)).getValue().longValue(), 1231341535);
        assertEquals(new Int64("-2341").getValue().longValue(), -2341);
    }
}