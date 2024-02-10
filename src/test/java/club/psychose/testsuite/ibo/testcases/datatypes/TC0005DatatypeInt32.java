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

import club.psychose.library.ibo.core.datatypes.types.signed.Int32;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TC0005DatatypeInt32 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new Int32(Int32.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new Int32(Int32.getMaximumValue() + 1));

        // Storing and fetching values.
        Int32 int32 = new Int32(69420);
        Int32 secondInt32 = new Int32(649);

        long storedValue = int32.getValue();
        assertEquals(storedValue, 69420);
        storedValue = 1111;
        assertEquals(storedValue, 1111);

        int32.setValue(649);
        assertEquals(int32.getValue(), 649);
        assertEquals(int32.getValue(), secondInt32.getValue());
        assertEquals(int32.toString(), "649");

        String hexString = int32.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals(hexString, "89020000");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(14124).array();
        assertEquals(new Int32(bytesWithoutSetByteOrder).getValue(), 14124);

        byte[] bytesWithByteOrder = new byte[124];
        bytesWithByteOrder[0] = 0x00;
        bytesWithByteOrder[1] = 0x0F;
        bytesWithByteOrder[2] = 0x34;
        bytesWithByteOrder[3] = 0x68;

        assertEquals(new Int32(bytesWithByteOrder, ByteOrder.BIG_ENDIAN).getValue(), 996456);
        assertEquals(new Int32((byte) 90).getValue(), 90);
        assertEquals(new Int32((short) 124).getValue(), 124);
        assertEquals(new Int32((long) 1254123513).getValue(), 1254123513);
        assertEquals(new Int32(2411f).getValue(), (int) 2411f);
        assertEquals(new Int32(1641.3).getValue(), 1641);
        assertEquals(new Int32(BigInteger.valueOf(13513512)).getValue(), 13513512);
        assertEquals(new Int32("1351351210").getValue(), 1351351210);
    }
}