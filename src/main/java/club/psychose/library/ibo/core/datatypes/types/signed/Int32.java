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

package club.psychose.library.ibo.core.datatypes.types.signed;

import club.psychose.library.ibo.core.datatypes.IBODataType;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * This class handles the Int32 data type.
 */

public final class Int32 extends IBODataType<Long> {
    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first four bytes, more bytes will be thrown away.<p>
     * The default {@link ByteOrder} is the native order.
     * @param dataBytes The bytes that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(0L);

        if (dataBytes.length < 4)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than four bytes.");

        if (dataBytes.length > 4) {
            byte[] newBytes = new byte[4];

            for (byte index = 0; index < 4; index ++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue((long) ByteBuffer.wrap(dataBytes, 0, 4).order(ByteOrder.nativeOrder()).getInt());
    }

    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first four bytes, more bytes will be thrown away.
     * @param dataBytes The bytes that should be interpreted as {@link Int32}.
     * @param byteOrder The used {@link ByteOrder}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(0L);

        if (dataBytes.length < 4)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than four bytes.");

        if (dataBytes.length > 4) {
            byte[] newBytes = new byte[4];

            for (byte index = 0; index < 4; index ++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue((long) ByteBuffer.wrap(dataBytes, 0, 4).order(byteOrder).getInt());
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (byte value) throws RangeOutOfBoundsException {
        super((long) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (short value) throws RangeOutOfBoundsException {
        super((long) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (int value) throws RangeOutOfBoundsException {
        super((long) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (long value) throws RangeOutOfBoundsException {
        super(value);
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (float value) throws RangeOutOfBoundsException {
        super((long) value);
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (double value) throws RangeOutOfBoundsException {
        super((long) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (BigInteger value) throws RangeOutOfBoundsException {
        super(value.longValue());
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 (String value) throws RangeOutOfBoundsException {
        super(Long.parseLong(value));
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (byte value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (long) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (long) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (long) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public void setValue (Long value) throws RangeOutOfBoundsException {
        this.setValue((long) value);
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (long) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (long) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        BigInteger convertedMinimumValue = BigInteger.valueOf(getMinimumValue());
        BigInteger convertedMaximumValue = BigInteger.valueOf(getMaximumValue());

        if (((value.compareTo(convertedMinimumValue) > 0) || (value.equals(convertedMinimumValue))) && ((value.compareTo(convertedMaximumValue) < 0) || (value.equals(convertedMaximumValue)))) {
            this.dataObject = value.longValue();
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link Int32}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (String value) throws RangeOutOfBoundsException {
        long convertedValue = Long.parseLong(value);

        if ((convertedValue >= getMinimumValue()) && (convertedValue <= getMaximumValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int32 data type is out of bounds!");
        }
    }

    /**
     * This method returns the data type as an array of {@link Byte}.
     * @param byteOrder ByteOrder for the bytes.
     * @return An array of {@link Byte}
     */
    @Override
    public byte[] getAsBytes (ByteOrder byteOrder) throws RangeOutOfBoundsException {
        byte[] longBytes = (byteOrder != null) ? (ByteBuffer.allocate(getByteLength() * 2).order(byteOrder).putLong(this.dataObject).array()) : (ByteBuffer.allocate(getByteLength() * 2).order(ByteOrder.nativeOrder()).putLong(this.dataObject).array());
        boolean isValueNegative = (this.dataObject < 0);
        return this.extractBytes(longBytes, byteOrder, 4, 4, isValueNegative);
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     * @param int32 The other data object.
     * @return True or False
     */
    public boolean equals (Int32 int32) {
        return Objects.equals(this.dataObject, int32.getValue());
    }

    /**
     * This method returns the length of the data type.
     * @return {@link Short}
     */
    public static short getByteLength () {
        return 4;
    }

    /**
     * This method returns the minimum value for this data type.
     * @return {@link Long}
     */
    public static long getMinimumValue () {
        return -2147483648;
    }

    /**
     * This method returns the maximum value for this data type.
     * @return {@link Long}
     */
    public static long getMaximumValue () {
        return 2147483647;
    }
}