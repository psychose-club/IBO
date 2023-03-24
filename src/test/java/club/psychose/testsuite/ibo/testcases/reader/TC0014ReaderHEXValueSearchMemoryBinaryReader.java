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

package club.psychose.testsuite.ibo.testcases.reader;

import club.psychose.library.ibo.MemoryBinaryReader;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;

import java.nio.ByteOrder;

public final class TC0014ReaderHEXValueSearchMemoryBinaryReader extends Test {
    public TC0014ReaderHEXValueSearchMemoryBinaryReader () {
        super("TC_0014_READER_HEXVALUESEARCH_MEMORYBINARYREADER");
    }

    @Override
    public void executeTestCase () {
        byte[] bytes = this.setupByteArray();

        MemoryBinaryReader memoryBinaryReader = new MemoryBinaryReader(ByteOrder.LITTLE_ENDIAN);

        try {
            memoryBinaryReader.open(bytes);

            if (memoryBinaryReader.getBinaryLength() != 0x30) {
                this.failed("INVALID_BINARY_LENGTH");
                return;
            }

            int firstOffsetPosition = memoryBinaryReader.searchFirstHEXValue("48656C6C6F21");

            if (firstOffsetPosition != 0x26) {
                this.failed("FIRST_OFFSET_POSITION_INVALID");
                return;
            }

            int secondOffsetPosition = memoryBinaryReader.searchFirstHEXValue("54686973", 0x04);

            if (secondOffsetPosition != 0x20) {
                this.failed("SECOND_OFFSET_POSITION_INVALID");
                return;
            }

            this.passed();
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS_EXCEPTION");
            rangeOutOfBoundsException.printStackTrace();
        } finally {
            memoryBinaryReader.close();
        }
    }

    private byte[] setupByteArray () {
        byte[] memoryBytes = new byte[0x30];

        memoryBytes[0] = 0x54;
        memoryBytes[1] = 0x68;
        memoryBytes[2] = 0x69;
        memoryBytes[3] = 0x73;
        memoryBytes[4] = 0x20;
        memoryBytes[5] = 0x69;
        memoryBytes[6] = 0x73;
        memoryBytes[7] = 0x20;
        memoryBytes[8] = 0x6E;
        memoryBytes[9] = 0x6F;
        memoryBytes[10] = 0x74;
        memoryBytes[11] = 0x20;
        memoryBytes[12] = 0x72;
        memoryBytes[13] = 0x65;
        memoryBytes[14] = 0x71;
        memoryBytes[15] = 0x75;
        memoryBytes[16] = 0x69;
        memoryBytes[17] = 0x72;
        memoryBytes[18] = 0x65;
        memoryBytes[19] = 0x64;
        memoryBytes[20] = 0x20;
        memoryBytes[21] = 0x62;
        memoryBytes[22] = 0x75;
        memoryBytes[23] = 0x74;
        memoryBytes[24] = 0x20;
        memoryBytes[25] = 0x69;
        memoryBytes[26] = 0x20;
        memoryBytes[27] = 0x77;
        memoryBytes[28] = 0x61;
        memoryBytes[29] = 0x6E;
        memoryBytes[30] = 0x74;
        memoryBytes[31] = 0x20;
        memoryBytes[32] = 0x54;
        memoryBytes[33] = 0x68;
        memoryBytes[34] = 0x69;
        memoryBytes[35] = 0x73;
        memoryBytes[36] = 0x3A;
        memoryBytes[37] = 0x20;
        memoryBytes[38] = 0x48;
        memoryBytes[39] = 0x65;
        memoryBytes[40] = 0x6C;
        memoryBytes[41] = 0x6C;
        memoryBytes[42] = 0x6F;
        memoryBytes[43] = 0x21;
        memoryBytes[44] = 0x20;
        memoryBytes[45] = 0x79;
        memoryBytes[46] = 0x65;
        memoryBytes[47] = 0x73;

        return memoryBytes;
    }
}