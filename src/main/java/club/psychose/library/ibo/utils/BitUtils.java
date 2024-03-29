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

package club.psychose.library.ibo.utils;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

/**
 * This class handles methods that are related with bits.
 */
public final class BitUtils {
    /**
     * This method extracts the bits from a {@link Int8}.
     *
     * @param int8          The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (Int8 int8, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(int8.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt8}.
     *
     * @param uInt8         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (UInt8 uInt8, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(uInt8.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int16}.
     *
     * @param int16         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (Int16 int16, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(int16.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt16}.
     *
     * @param uInt16        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (UInt16 uInt16, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(uInt16.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int24}.
     *
     * @param int24         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (Int24 int24, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(int24.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt24}.
     *
     * @param uInt24        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (UInt24 uInt24, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(uInt24.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int32}.
     *
     * @param int32         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (Int32 int32, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(int32.getValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt32}.
     *
     * @param uInt32        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (UInt32 uInt32, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(uInt32.getValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int64}.
     *
     * @param int64         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (Int64 int64, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(int64.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt64}.
     *
     * @param uInt64        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (UInt64 uInt64, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits(uInt64.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Byte}.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (byte value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Byte.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Short}.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (short value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Short.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Integer}.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (int value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Integer.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBits((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Integer}.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBits (long value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Long.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        long bitMask = ((1L << (endBitIndex - startBitIndex + 1)) - 1) << startBitIndex;
        return (value & bitMask) >>> startBitIndex;
    }
}