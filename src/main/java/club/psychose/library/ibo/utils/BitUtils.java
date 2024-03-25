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
     * This method extracts the bits from a {@link Int8} via the start and end index.
     *
     * @param int8          The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (Int8 int8, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(int8.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int8} via the start and end number.
     *
     * @param int8           The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (Int8 int8, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Int8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(int8.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link UInt8} via the start and end index.
     *
     * @param uInt8         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (UInt8 uInt8, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(uInt8.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt8} via the start and end number.
     *
     * @param uInt8          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (UInt8 uInt8, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > UInt8.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(uInt8.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Int16} via the start and end index.
     *
     * @param int16         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (Int16 int16, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(int16.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int16} via the start and end number.
     *
     * @param int16          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (Int16 int16, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Int16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(int16.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link UInt16} via the start and end index.
     *
     * @param uInt16        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (UInt16 uInt16, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(uInt16.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt16} via the start and end number.
     *
     * @param uInt16         The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (UInt16 uInt16, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > UInt16.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(uInt16.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Int24} via the start and end index.
     *
     * @param int24         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (Int24 int24, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(int24.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int24} via the start and end number.
     *
     * @param int24          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (Int24 int24, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Int24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(int24.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link UInt24} via the start and end index.
     *
     * @param uInt24        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (UInt24 uInt24, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(uInt24.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt24} via the start and end number.
     *
     * @param uInt24         The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (UInt24 uInt24, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > UInt24.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(uInt24.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Int32} via the start and end index.
     *
     * @param int32         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (Int32 int32, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(int32.getValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int32} via the start and end number.
     *
     * @param int32          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (Int32 int32, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Int32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(int32.getValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link UInt32} via the start and end index.
     *
     * @param uInt32        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (UInt32 uInt32, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(uInt32.getValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt32} via the start and end number.
     *
     * @param uInt32         The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (UInt32 uInt32, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > UInt32.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(uInt32.getValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Int64} via the start and end index.
     *
     * @param int64         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (Int64 int64, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Int64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(int64.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Int64} via the start and end number.
     *
     * @param int64          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (Int64 int64, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Int64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(int64.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link UInt64} via the start and end index.
     *
     * @param uInt64        The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (UInt64 uInt64, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= UInt64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex(uInt64.getValue().longValue(), startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link UInt64} via the start and end number.
     *
     * @param uInt64         The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (UInt64 uInt64, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > UInt64.getBitLength()))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber(uInt64.getValue().longValue(), startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Byte} via the start and end index.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (byte value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Byte.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Byte} via the start and end number.
     *
     * @param value          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (byte value, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Byte.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber((long) value, startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Short} via the start and end index.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (short value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Short.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Short} via the start and end number.
     *
     * @param value          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (short value, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Short.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber((long) value, startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Integer} via the start and end index.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (int value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Integer.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        return extractBitsViaIndex((long) value, startBitIndex, endBitIndex);
    }

    /**
     * This method extracts the bits from a {@link Integer} via the start and end number.
     *
     * @param value          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (int value, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Integer.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        return extractBitsViaNumber((long) value, startBitNumber, endBitNumber);
    }

    /**
     * This method extracts the bits from a {@link Long} via the start and end index.
     *
     * @param value         The value to extract the bits from.
     * @param startBitIndex The index where the extraction should be started from.
     * @param endBitIndex   The index where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaIndex (long value, int startBitIndex, int endBitIndex) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitIndex < 0) || (endBitIndex < startBitIndex) || (endBitIndex >= Long.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an index is out of bounds!");

        long bitMask = ((1L << (endBitIndex - startBitIndex + 1)) - 1) << startBitIndex;
        return (value & bitMask) >>> startBitIndex;
    }

    /**
     * This method extracts the bits from a {@link Long} via the start and end number.
     *
     * @param value          The value to extract the bits from.
     * @param startBitNumber The number where the extraction should be started from.
     * @param endBitNumber   The number where the extraction should be ended.
     *
     * @return The result of the bit extraction.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public static long extractBitsViaNumber (long value, int startBitNumber, int endBitNumber) throws RangeOutOfBoundsException {
        // Check if the bits can be extracted.
        if ((startBitNumber < 1) || (endBitNumber < startBitNumber) || (endBitNumber > Long.SIZE))
            throw new RangeOutOfBoundsException("The bits can't be extracted because an number is out of bounds!");

        long bitMask = ((1L << (endBitNumber - startBitNumber + 1)) - 1) << (startBitNumber - 1);
        return (value & bitMask) >>> (startBitNumber - 1);
    }
}