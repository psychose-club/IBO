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

package club.psychose.library.ibo.datatypes.types.unsigned;

import club.psychose.library.ibo.datatypes.IBODataType;
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
    public UInt64 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(0));
        this.setValue(new BigInteger(1, ByteBuffer.wrap(this.getBytesAsBigEndianByteOrder(dataBytes, null), 0, 8).array()));
    }

    public UInt64 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(0));
        this.setValue(new BigInteger(1, ByteBuffer.wrap(this.getBytesAsBigEndianByteOrder(dataBytes, byteOrder), 0, 8).array()));
    }

    public UInt64 (byte value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value & 0xFF));
    }

    public UInt64 (short value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public UInt64 (int value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public UInt64 (long value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public UInt64 (float value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    public UInt64 (double value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    public UInt64 (BigInteger value) throws RangeOutOfBoundsException {
        super(value);
    }

    public UInt64 (String value) throws RangeOutOfBoundsException {
        super(new BigInteger(value));
    }

    public void setValue (byte value) throws RangeOutOfBoundsException {
        BigInteger convertedValue = BigInteger.valueOf(value & 0xFF);

        if ((convertedValue.longValue() >= getMinimumValue().longValue()) && (convertedValue.longValue() <= getMaximumValue().longValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    @Override
    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        if (((value.compareTo(getMinimumValue()) > 0) || (value.equals(getMinimumValue()))) && ((value.compareTo(getMaximumValue()) < 0) || (value.equals(getMaximumValue())))) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt64 data type is out of bounds!");
        }
    }

    public void setValue (String value) throws RangeOutOfBoundsException {
        BigInteger convertedValue = new BigInteger(value);
        this.setValue(convertedValue);
    }

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
     * @param uInt64 The other data object.
     * @return True or False
     */
    public boolean equals (UInt64 uInt64) {
        return Objects.equals(this.dataObject, uInt64.getValue());
    }

    public static short getByteLength () {
        return 8;
    }

    public static BigInteger getMinimumValue () {
        return BigInteger.valueOf(0);
    }

    public static BigInteger getMaximumValue () {
        return new BigInteger("18446744073709551615");
    }
}