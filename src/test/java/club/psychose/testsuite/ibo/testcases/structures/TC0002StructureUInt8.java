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

import club.psychose.library.ibo.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.nio.ByteOrder;

public final class TC0002StructureUInt8 extends Test {
    public TC0002StructureUInt8 () {
        super("TC_0002_STRUCTURE_UINT8");
    }

    @Override
    public void executeTestCase () {
        // Out of Bounds Test.
        try {
            new UInt8(UInt8.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new UInt8(UInt8.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            UInt8 uInt8 = new UInt8(213);
            short storedValue = uInt8.getValue();

            if (storedValue != 213) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 69;

            if (uInt8.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            uInt8.setValue(42);
            UInt8 secondUInt8 = new UInt8((double) 42);

            if (uInt8.getValue() != (byte) 42) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(uInt8.equals(secondUInt8))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = uInt8.getAsString();
            if (!(valueAsString.equals("42"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = uInt8.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.BIG_ENDIAN);

            if (!(hexString.equals("2A"))) {
                this.failed("CONVERT_TO_HEX_STRING");
                return;
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("STORING_AND_FETCHING");
            return;
        }

        this.passed();
    }
}