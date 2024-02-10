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

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TC0002DatatypeUInt8 {
    @Test
    public void executeTestCase () {
        // Out-of-Bounds Test.
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt8(UInt8.getMinimumValue() - 1));
        assertThrows(RangeOutOfBoundsException.class, () -> new UInt8(UInt8.getMaximumValue() + 1));

        // Storing and fetching values.
        UInt8 uInt8 = new UInt8(213);
        UInt8 secondUInt8 = new UInt8((double) 42);

        short storedValue = uInt8.getValue();
        assertEquals(storedValue, 213);
        storedValue = 69;
        assertEquals(storedValue, 69);

        uInt8.setValue(42);
        assertEquals(uInt8.getValue(), (short) 42);
        assertEquals(uInt8.getValue(), secondUInt8.getValue());
        assertEquals(uInt8.toString(), "42");

        String hexString = uInt8.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);
        assertEquals(hexString, "2A");

        // Checking the other constructors.
        byte[] bytes = new byte[153];
        bytes[0] = 0x10;

        assertEquals(new UInt8(bytes).getValue(), (short) 0x10);
        assertEquals(new UInt8((byte) 2).getValue(), (short) 2);
        assertEquals(new UInt8((short) 149).getValue(), (short) 149);
        assertEquals(new UInt8((long) 242).getValue(), (short) 242);
        assertEquals(new UInt8(24f).getValue(), (short) 24f);
        assertEquals(new UInt8(123.5d).getValue(), (short) 123);
        assertEquals(new UInt8(BigInteger.ONE).getValue(), (short) 1);
        assertEquals(new UInt8("234").getValue(), (short) 234);
    }
}