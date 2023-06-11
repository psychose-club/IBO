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
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class TC0008DatatypeUInt64 extends Test {
    public TC0008DatatypeUInt64() {
        super("TC_0008_DATATYPE_UINT64");
    }

    @Override
    public void executeTestCase () {
        // Out of Bounds Test.
        try {
            new UInt64(new BigInteger(UInt64.getMinimumValue().toString()).subtract(BigInteger.valueOf(1)));
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new UInt64(new BigInteger(UInt64.getMaximumValue().toString()).add(BigInteger.valueOf(1)));
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            UInt64 uInt64 = new UInt64(UInt64.getMaximumValue());
            BigInteger storedValue = uInt64.getValue();

            if (!(storedValue.equals(UInt64.getMaximumValue()))) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = BigInteger.valueOf(41541555);

            if (uInt64.getValue().equals(storedValue)) {
                this.failed("COMPARING_VALUE");
                return;
            }

            uInt64.setValue(UInt64.getMinimumValue());
            UInt64 secondUInt64 = new UInt64(UInt64.getMinimumValue());

            if (!(uInt64.getValue().equals(UInt64.getMinimumValue()))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(uInt64.equals(secondUInt64))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = uInt64.getAsString();
            if (!(valueAsString.equals(UInt64.getMinimumValue().toString()))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = uInt64.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);

            if (!(hexString.equals("0000000000000000"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }

            // Check the other constructors.
            byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(249664).array();
            if (new UInt64(bytesWithoutSetByteOrder).getValue().longValue() != 249664) {
                this.failed("OTHER_CONSTRUCTORS_01");
                return;
            }

            byte[] bytesWithByteOrder = new byte[123];
            bytesWithByteOrder[0] = 0x11;
            bytesWithByteOrder[1] = 0x18;
            bytesWithByteOrder[2] = 0x00;
            bytesWithByteOrder[3] = 0x00;
            bytesWithByteOrder[4] = 0x00;
            bytesWithByteOrder[5] = 0x00;
            bytesWithByteOrder[6] = 0x00;
            bytesWithByteOrder[7] = 0x00;
            if (new UInt64(bytesWithByteOrder, ByteOrder.LITTLE_ENDIAN).getValue().longValue() != 6161) {
                this.failed("OTHER_CONSTRUCTORS_02");
                return;
            }

            if (new UInt64((byte) 13).getValue().longValue() != 13) {
                this.failed("OTHER_CONSTRUCTORS_03");
                return;
            }

            if (new UInt64((short) 21).getValue().longValue() != 21) {
                this.failed("OTHER_CONSTRUCTORS_04");
                return;
            }

            if (new UInt64((long) 2222).getValue().longValue() != 2222) {
                this.failed("OTHER_CONSTRUCTORS_05");
                return;
            }

            if (new UInt64(0.1f).getValue().longValue() != 0f) {
                this.failed("OTHER_CONSTRUCTORS_06");
                return;
            }

            if (new UInt64(0.69).getValue().longValue() != 0) {
                this.failed("OTHER_CONSTRUCTORS_07");
                return;
            }

            if (new UInt64(BigInteger.valueOf(1234)).getValue().longValue() != 1234) {
                this.failed("OTHER_CONSTRUCTORS_08");
                return;
            }

            if (new UInt64("42").getValue().longValue() != 42) {
                this.failed("OTHER_CONSTRUCTORS_09");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STORING_AND_FETCHING");
            return;
        }

        this.passed();
    }
}