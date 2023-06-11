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

package club.psychose.testsuite.ibo.testcases.structures;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class TC0004StructureUInt16 extends Test {
    public TC0004StructureUInt16 () {
        super("TC_0004_STRUCTURE_UINT16");
    }

    @Override
    public void executeTestCase () {
        // Out of Bounds Test.
        try {
            new UInt16(UInt16.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new UInt16(UInt16.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            UInt16 uint16 = new UInt16(1337);
            int storedValue = uint16.getValue();

            if (storedValue != 1337) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 420;

            if (uint16.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            uint16.setValue(12);
            UInt16 secondUInt16 = new UInt16(12);

            if (uint16.getValue() != 12) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(uint16.equals(secondUInt16))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = uint16.getAsString();
            if (!(valueAsString.equals("12"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = uint16.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);

            if (!(hexString.equals("0C00"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }

            // Check the other constructors.
            byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort((short) 2641).array();
            if (new UInt16(bytesWithoutSetByteOrder).getValue() != 2641) {
                this.failed("OTHER_CONSTRUCTORS_01");
                return;
            }

            byte[] bytesWithByteOrder = new byte[124];
            bytesWithByteOrder[0] = 0x4F;
            bytesWithByteOrder[1] = 0x15;
            if (new UInt16(bytesWithByteOrder, ByteOrder.LITTLE_ENDIAN).getValue() != 5455) {
                this.failed("OTHER_CONSTRUCTORS_02");
                return;
            }

            if (new UInt16((byte) 232).getValue() != 232) {
                this.failed("OTHER_CONSTRUCTORS_03");
                return;
            }

            if (new UInt16((short) 1451).getValue() != 1451) {
                this.failed("OTHER_CONSTRUCTORS_04");
                return;
            }

            if (new UInt16((long) 23414).getValue() != 23414) {
                this.failed("OTHER_CONSTRUCTORS_05");
                return;
            }

            if (new UInt16(2344.141f).getValue() != 2344f) {
                this.failed("OTHER_CONSTRUCTORS_06");
                return;
            }

            if (new UInt16((double) 6453).getValue() != 6453) {
                this.failed("OTHER_CONSTRUCTORS_07");
                return;
            }

            if (new UInt16(BigInteger.valueOf(65535)).getValue() != 65535) {
                this.failed("OTHER_CONSTRUCTORS_08");
                return;
            }

            if (new UInt16("5235").getValue() != 5235) {
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