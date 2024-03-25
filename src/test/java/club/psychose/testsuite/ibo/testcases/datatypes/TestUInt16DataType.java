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

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TestUInt16DataType {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt16(UInt16.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt16(UInt16.getMaximumValue() + 1));

        // Storing and fetching values.
        UInt16 uInt16 = new UInt16(1337);
        UInt16 secondUInt16 = new UInt16(12);

        int storedValue = uInt16.getValue();
        assertEquals(1337, storedValue);
        storedValue = 420;
        assertEquals(420, storedValue);

        uInt16.setValue(12);
        assertEquals(12, uInt16.getValue());
        assertEquals(uInt16.getValue(), secondUInt16.getValue());
        assertEquals("12", uInt16.toString());

        String hexString = uInt16.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);
        assertEquals("0C00", hexString);

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort((short) 2641).array();
        assertEquals(2641, new UInt16(bytesWithoutSetByteOrder).getValue());

        byte[] bytesWithByteOrder = new byte[124];
        bytesWithByteOrder[0] = 0x4F;
        bytesWithByteOrder[1] = 0x15;

        assertEquals(5455, new UInt16(bytesWithByteOrder, ByteOrder.LITTLE_ENDIAN).getValue());
        assertEquals(232, new UInt16((byte) 232).getValue());
        assertEquals(1451, new UInt16((short) 1451).getValue());
        assertEquals(23414, new UInt16((long) 23414).getValue());
        assertEquals((int) 2344f, new UInt16(2344.141f).getValue());
        assertEquals(6453, new UInt16((double) 6453).getValue());
        assertEquals(65535, new UInt16(BigInteger.valueOf(65535)).getValue());
        assertEquals(5235, new UInt16("5235").getValue());
    }
}