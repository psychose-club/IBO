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

package club.psychose.library.ibo.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class handles methods for arrays.
 */

public final class ArrayUtils {
    /**
     * This method reverses an array.
     * @param byteArray The byte array to reverse.
     * @return The reverse byte array.
     */
    public static byte[] reverseByteArray (byte[] byteArray) {
        byte[] reversedByteArray = new byte[byteArray.length];
        IntStream.range(0, byteArray.length).forEachOrdered(index -> reversedByteArray[index] = byteArray[byteArray.length - index - 1]);
        return reversedByteArray;
    }

    /**
     * This method splits the bytes from a HEX string into pairs of two.
     * @param hexString The string that should be split.
     * @return {@link List<String>}
     */
    public static List<String> splitHEXByteCharacters (String hexString) {
        String[] characters = hexString.split("(?<=\\G.{2})");
        return Arrays.asList(characters);
    }
}