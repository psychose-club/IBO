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

import club.psychose.library.ibo.core.datatypes.types.signed.Int32;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class TC0005StructureInt32 extends Test {
    public TC0005StructureInt32 () {
        super("TC_0005_STRUCTURE_INT32");
    }

    @Override
    public void executeTestCase () {
        // Out of Bounds Test.
        try {
            new Int32(Int32.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new Int32(Int32.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            Int32 int32 = new Int32(69420);
            long storedValue = int32.getValue();

            if (storedValue != 69420) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 1111;

            if (int32.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            int32.setValue(649);
            Int32 secondInt32 = new Int32(649);

            if (int32.getValue() != 649) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(int32.equals(secondInt32))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = int32.getAsString();
            if (!(valueAsString.equals("649"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = int32.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);

            if (!(hexString.equals("89020000"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }

            // Check the other constructors.
            byte[] bytesWithoutSetByteOrder = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(14124).array();
            if (new Int32(bytesWithoutSetByteOrder).getValue() != 14124) {
                this.failed("OTHER_CONSTRUCTORS_01");
                return;
            }

            byte[] bytesWithByteOrder = new byte[124];
            bytesWithByteOrder[0] = 0x00;
            bytesWithByteOrder[1] = 0x0F;
            bytesWithByteOrder[2] = 0x34;
            bytesWithByteOrder[3] = 0x68;
            if (new Int32(bytesWithByteOrder, ByteOrder.BIG_ENDIAN).getValue() != 996456) {
                this.failed("OTHER_CONSTRUCTORS_02");
                return;
            }

            if (new Int32((byte) 90).getValue() != 90) {
                this.failed("OTHER_CONSTRUCTORS_03");
                return;
            }

            if (new Int32((short) 124).getValue() != 124) {
                this.failed("OTHER_CONSTRUCTORS_04");
                return;
            }

            if (new Int32((long) 1254123513).getValue() != 1254123513) {
                this.failed("OTHER_CONSTRUCTORS_05");
                return;
            }

            if (new Int32(2411f).getValue() != 2411) {
                this.failed("OTHER_CONSTRUCTORS_06");
                return;
            }

            if (new Int32(1641.3).getValue() != 1641) {
                this.failed("OTHER_CONSTRUCTORS_07");
                return;
            }

            if (new Int32(BigInteger.valueOf(13513512)).getValue() != 13513512) {
                this.failed("OTHER_CONSTRUCTORS_08");
                return;
            }

            if (new Int32("1351351210").getValue() != 1351351210) {
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