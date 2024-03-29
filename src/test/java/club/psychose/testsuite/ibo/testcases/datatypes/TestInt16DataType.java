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

import club.psychose.library.ibo.core.datatypes.types.signed.Int16;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TestInt16DataType {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new Int16(Int16.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new Int16(Int16.getMaximumValue() + 1));

        // Storing and fetching values.
        Int16 int16 = new Int16(1234);
        Int16 secondInt16 = new Int16(BigInteger.valueOf(4333L));

        int storedValue = int16.getValue();
        assertEquals(storedValue, 1234);
        storedValue = 77;
        assertEquals(storedValue, 77);

        int16.setValue(4333);
        assertEquals(int16.getValue(), 4333);
        assertEquals(int16.getValue(), secondInt16.getValue());
        assertEquals(int16.toString(), "4333");

        String hexString = int16.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals(hexString, "ED10");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort((short) 24).array();
        assertEquals(new Int16(bytesWithoutSetByteOrder).getValue(), 24);

        byte[] bytesWithByteOrder = new byte[53];
        bytesWithByteOrder[0] = 0x50;
        bytesWithByteOrder[1] = (byte) 0xFE;

        assertEquals(new Int16(bytesWithByteOrder, ByteOrder.LITTLE_ENDIAN).getValue(), -432);
        assertEquals(new Int16((byte) -22).getValue(), -22);
        assertEquals(new Int16((short) -149).getValue(), -149);
        assertEquals(new Int16((long) 2432).getValue(), 2432);
        assertEquals(new Int16(234.54f).getValue(), (int) 234f);
        assertEquals(new Int16(723.61446).getValue(), 723);
        assertEquals(new Int16(BigInteger.valueOf(32767)).getValue(), 32767);
        assertEquals(new Int16("123").getValue(), 123);
    }
}