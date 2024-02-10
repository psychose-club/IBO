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

import club.psychose.library.ibo.core.datatypes.types.signed.Int8;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TestInt8DataType {
    @Test
    public void executeTest () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new Int8(Int8.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new Int8(Int8.getMaximumValue() + 1));

        // Storing and fetching values.
        Int8 int8 = new Int8(55);
        Int8 secondInt8 = new Int8((float) -22);

        short storedValue = int8.getValue();
        assertEquals(storedValue, 55);
        storedValue = 9;
        assertEquals(storedValue, 9);

        int8.setValue(-22);
        assertEquals(int8.getValue(), (short) -22);
        assertEquals(int8.getValue(), secondInt8.getValue());
        assertEquals(int8.toString(), "-22");

        String hexString = int8.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);
        assertEquals(hexString, "EA");

        // Checking the other constructors.
        byte[] bytes = new byte[2];
        bytes[0] = 0x02;

        assertEquals(new Int8(bytes).getValue(), (short) 0x02);
        assertEquals(new Int8((byte) 33).getValue(), (short) 33);
        assertEquals(new Int8((short) 69).getValue(), (short) 69);
        assertEquals(new Int8((long) 49).getValue(), (short) 49);
        assertEquals(new Int8(-23f).getValue(), (short) -23f);
        assertEquals(new Int8(8.4234d).getValue(), (short) 8);
        assertEquals(new Int8(BigInteger.TEN).getValue(), (short) 10);
        assertEquals(new Int8("-54").getValue(), (short) -54);
    }
}