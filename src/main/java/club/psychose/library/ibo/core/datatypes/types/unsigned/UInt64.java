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

package club.psychose.library.ibo.core.datatypes.types.unsigned;

import club.psychose.library.ibo.core.datatypes.IBODataType;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class handles the UInt64 data type.
 */
public final class UInt64 extends IBODataType<BigInteger> {
    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first eight bytes, more bytes will be thrown away.<p>
     * The default {@link ByteOrder} is the native order.
     *
     * @param dataBytes The bytes that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(0));

        if (dataBytes.length < 8)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than eight bytes.");

        if (dataBytes.length > 8) {
            byte[] newBytes = new byte[8];

            for (byte index = 0; index < 8; index++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue(new BigInteger(1, ByteBuffer.wrap(this.getBytesAsBigEndianByteOrder(dataBytes, ByteOrder.nativeOrder()), 0, 8).array()));
    }

    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first eight bytes, more bytes will be thrown away.
     *
     * @param dataBytes The bytes that should be interpreted as {@link UInt64}.
     * @param byteOrder The used {@link ByteOrder}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(0));

        if (dataBytes.length < 8)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than eight bytes.");

        if (dataBytes.length > 8) {
            byte[] newBytes = new byte[8];

            for (byte index = 0; index < 8; index++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue(new BigInteger(1, ByteBuffer.wrap(this.getBytesAsBigEndianByteOrder(dataBytes, byteOrder), 0, 8).array()));
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (byte value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value & 0xFF));
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (short value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (int value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (long value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (float value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (double value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (BigInteger value) throws RangeOutOfBoundsException {
        super(value);
    }

    /**
     * The default constructor.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 (String value) throws RangeOutOfBoundsException {
        super(new BigInteger(value));
    }

    /**
     * This method returns the byte length of the data type.
     *
     * @return {@link Short}
     */
    public static short getByteLength () {
        return 8;
    }

    /**
     * This method returns the bit length of the data type.
     *
     * @return {@link Short}
     */
    public static short getBitLength () {
        return 64;
    }

    /**
     * This method returns the minimum value for this data type.
     *
     * @return {@link BigInteger}
     */
    public static BigInteger getMinimumValue () {
        return BigInteger.valueOf(0);
    }

    /**
     * This method returns the maximum value for this data type.
     *
     * @return {@link BigInteger}
     */
    public static BigInteger getMaximumValue () {
        return new BigInteger("18446744073709551615");
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (byte value) throws RangeOutOfBoundsException {
        BigInteger convertedValue = BigInteger.valueOf(value & 0xFF);

        if ((convertedValue.longValue() >= getMinimumValue().longValue()) && (convertedValue.longValue() <= getMaximumValue().longValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        if (((value.compareTo(getMinimumValue()) > 0) || (value.equals(getMinimumValue()))) && ((value.compareTo(getMaximumValue()) < 0) || (value.equals(getMaximumValue())))) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     *
     * @param value The value that should be interpreted as {@link UInt64}.
     *
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (String value) throws RangeOutOfBoundsException {
        BigInteger convertedValue = new BigInteger(value);
        this.setValue(convertedValue);
    }

    /**
     * This method returns the data type as an array of {@link Byte}.
     *
     * @param byteOrder ByteOrder for the bytes.
     *
     * @return An array of {@link Byte}
     */
    @Override
    public byte[] getAsBytes (ByteOrder byteOrder) throws RangeOutOfBoundsException {
        if (byteOrder == null)
            byteOrder = ByteOrder.nativeOrder();

        byte[] byteArray = this.dataObject.toByteArray();
        byte[] buffer = new byte[8];

        boolean isValueNegative = (this.dataObject.compareTo(BigInteger.ZERO) < 0);
        IntStream.range(0, (8 - byteArray.length)).forEachOrdered(index -> buffer[index] = (isValueNegative) ? ((byte) 0xFF) : ((byte) 0x0));
        IntStream.range((8 - byteArray.length), 8).forEachOrdered(index -> buffer[index] = byteArray[(index - (8 - byteArray.length))]);

        return this.getBytesAsBigEndianByteOrder(buffer, byteOrder);
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     *
     * @param uInt64 The other data object.
     *
     * @return True or False
     */
    public boolean equals (UInt64 uInt64) {
        return Objects.equals(this.dataObject, uInt64.getValue());
    }
}