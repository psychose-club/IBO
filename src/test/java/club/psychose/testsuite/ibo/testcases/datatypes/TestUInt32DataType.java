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

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TestUInt32DataType {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt32(UInt32.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt32(UInt32.getMaximumValue() + 1));

        // Storing and fetching values.
        UInt32 uint32 = new UInt32(41441041);
        UInt32 secondUInt32 = new UInt32(465774);

        long storedValue = uint32.getValue();
        assertEquals(storedValue, 41441041);
        storedValue = 463456;
        assertEquals(storedValue, 463456);

        uint32.setValue("465774");
        assertEquals(uint32.getValue(), 465774);
        assertEquals(uint32.getValue(), secondUInt32.getValue());
        assertEquals(uint32.toString(), "465774");

        String hexString = uint32.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);
        assertEquals(hexString, "00071B6E");

        // Checking the other constructors.
        byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(5441854).array();
        assertEquals(new UInt32(bytesWithoutSetByteOrder).getValue(), 5441854);

        byte[] bytesWithByteOrder = new byte[124];
        bytesWithByteOrder[0] = 0x00;
        bytesWithByteOrder[1] = 0x0F;
        bytesWithByteOrder[2] = 0x34;
        bytesWithByteOrder[3] = 0x68;

        assertEquals(new UInt32(bytesWithByteOrder, ByteOrder.BIG_ENDIAN).getValue(), 996456);
        assertEquals(new UInt32((byte) 11).getValue(), 11);
        assertEquals(new UInt32((short) 124).getValue(), 124);
        assertEquals(new UInt32((long) 1254123513).getValue(), 1254123513);
        assertEquals(new UInt32(2512.2512f).getValue(), (int) 2512f);
        assertEquals(new UInt32(1641.3).getValue(), 1641);
        assertEquals(new UInt32(BigInteger.valueOf(13513512)).getValue(), 13513512);
        assertEquals(new UInt32("1351351210").getValue(), 1351351210);
    }
}