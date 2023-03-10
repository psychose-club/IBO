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

import club.psychose.library.ibo.datatypes.types.signed.Int16;
import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

public final class TC0003StructureInt16 extends Test {
    public TC0003StructureInt16 () {
        super("TC_0003_STRUCTURE_INT16");
    }

    @Override
    public void executeTestCase () {
        // Out of Bounds Test.
        try {
            new Int16(Int16.getMinimumValue() - 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        try {
            new Int16(Int16.getMaximumValue() + 1);
            this.failed("OUT_OF_BOUNDS_CHECK");
            return;
        } catch (RangeOutOfBoundsException ignoredException) {}

        // Storing and fetching values.
        try {
            Int16 int16 = new Int16(1234);
            int storedValue = int16.getValue();

            if (storedValue != 1234) {
                this.failed("ASSIGNING_VALUE");
                return;
            }

            storedValue = 77;

            if (int16.getValue() == storedValue) {
                this.failed("COMPARING_VALUE");
                return;
            }

            int16.setValue(4333);
            Int16 secondInt16 = new Int16(BigInteger.valueOf(4333L));

            if (int16.getValue() != 4333) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            if (!(int16.equals(secondInt16))) {
                this.failed("COMPARING_NEW_VALUE");
                return;
            }

            String valueAsString = int16.getAsString();
            if (!(valueAsString.equals("4333"))) {
                this.failed("CONVERT_TO_STRING");
                return;
            }

            // getAsBytes is executed in the HEX string method, so we don't check it here.
            String hexString = int16.getAsHEXString(HEXFormat.UPPERCASE, ByteOrder.LITTLE_ENDIAN);

            if (!(hexString.equals("ED10"))) {
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