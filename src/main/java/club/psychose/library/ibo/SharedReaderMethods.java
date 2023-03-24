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

package club.psychose.library.ibo;

import club.psychose.library.ibo.utils.ArrayUtils;

/**
 * This class provides method that is shared between the readers.
 */

class SharedReaderMethods {
    /**
     * This method removes padding from the provided bytes.
     * @param bytes The {@link Byte} array that should be removed the padding bytes.
     * @param paddingByte The {@link Byte} that should be removed
     * @param reverseRemoval If true the array will be internally reversed and the search for the padding byte will begin from behind. (Probably want you want)
     * @return The {@link Byte} array.
     */
    public byte[] removePaddingFromBytes (byte[] bytes, byte paddingByte, boolean reverseRemoval) {
        if (bytes == null)
            throw new NullPointerException("The provided bytes are null!");

        if (reverseRemoval)
            bytes = ArrayUtils.reverseByteArray(bytes);

        int originalLength = bytes.length;
        int newLength = bytes.length;

        for (byte arrayByte : bytes) {
            if (arrayByte != paddingByte)
                break;

            newLength --;
        }

        if (reverseRemoval)
            bytes = ArrayUtils.reverseByteArray(bytes);

        // When the original length is not equal to the new length the array will be shortened to the new length.
        if (originalLength != newLength) {
            byte[] newByteArray = new byte[newLength];

            if (!(reverseRemoval)) {
                int offset = (originalLength - newLength);

                if (offset < 0)
                    offset = 0;

                System.arraycopy(bytes, offset, newByteArray, 0, newLength);
            } else {
                System.arraycopy(bytes, 0, newByteArray, 0, newLength);
            }

            bytes = newByteArray;
        }

        return bytes;
    }
}