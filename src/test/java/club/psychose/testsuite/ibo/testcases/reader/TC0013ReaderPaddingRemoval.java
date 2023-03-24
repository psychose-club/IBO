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
import club.psychose.library.ibo.utils.HEXUtils;
import club.psychose.testsuite.ibo.testcases.Test;

public final class TC0013ReaderPaddingRemoval extends Test {
    public TC0013ReaderPaddingRemoval () {
        super("TC_0013_READER_PADDINGREMOVAL");
    }

    @Override
    public void executeTestCase () {
        // Because the method is in SharedReaderMethods we will only test it for MemoryBinaryReader (It is in the FileBinaryReader the same).
        byte[] bytes = this.setupByteArray();

        MemoryBinaryReader memoryBinaryReader = new MemoryBinaryReader();
        bytes = memoryBinaryReader.removePaddingFromBytes(bytes, (byte) 0x30, true);

        if (bytes.length != 35) {
            this.failed("INVALID_BYTES_SIZE");
            return;
        }

        bytes = memoryBinaryReader.removePaddingFromBytes(bytes, (byte) 0x30, false);

        if (bytes.length != 0x13) {
            this.failed("INVALID_BYTES_SIZE");
            return;
        }

        bytes = memoryBinaryReader.removePaddingFromBytes(bytes, (byte) 0x30, false);
        bytes = memoryBinaryReader.removePaddingFromBytes(bytes, (byte) 0x30, true);

        if (bytes.length != 0x13) {
            this.failed("INVALID_BYTES_SIZE");
            return;
        }

        if (!(HEXUtils.convertBytesToHEXString(bytes).equals("54686973206973206120636F6F6C2054657374"))) {
            this.failed("INVALID_HEX_STRING");
            return;
        }

        this.passed();
    }

    private byte[] setupByteArray () {
        byte[] bytes = new byte[0x30];

        bytes[0] = 0x30;
        bytes[1] = 0x30;
        bytes[2] = 0x30;
        bytes[3] = 0x30;
        bytes[4] = 0x30;
        bytes[5] = 0x30;
        bytes[6] = 0x30;
        bytes[7] = 0x30;
        bytes[8] = 0x30;
        bytes[9] = 0x30;
        bytes[10] = 0x30;
        bytes[11] = 0x30;
        bytes[12] = 0x30;
        bytes[13] = 0x30;
        bytes[14] = 0x30;
        bytes[15] = 0x30;
        bytes[16] = 0x54;
        bytes[17] = 0x68;
        bytes[18] = 0x69;
        bytes[19] = 0x73;
        bytes[20] = 0x20;
        bytes[21] = 0x69;
        bytes[22] = 0x73;
        bytes[23] = 0x20;
        bytes[24] = 0x61;
        bytes[25] = 0x20;
        bytes[26] = 0x63;
        bytes[27] = 0x6F;
        bytes[28] = 0x6F;
        bytes[29] = 0x6C;
        bytes[30] = 0x20;
        bytes[31] = 0x54;
        bytes[32] = 0x65;
        bytes[33] = 0x73;
        bytes[34] = 0x74;
        bytes[35] = 0x30;
        bytes[36] = 0x30;
        bytes[37] = 0x30;
        bytes[38] = 0x30;
        bytes[39] = 0x30;
        bytes[40] = 0x30;
        bytes[41] = 0x30;
        bytes[42] = 0x30;
        bytes[43] = 0x30;
        bytes[44] = 0x30;
        bytes[45] = 0x30;
        bytes[46] = 0x30;
        bytes[47] = 0x30;

        return bytes;
    }
}