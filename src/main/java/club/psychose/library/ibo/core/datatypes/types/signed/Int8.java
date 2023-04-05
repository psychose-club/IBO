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
 * This class handles the Int8 data type.
 */

public final class Int8 extends IBODataType<Short> {
    public Int8 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(ByteBuffer.wrap(dataBytes, 0, 2).getShort());
    }

    public Int8 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(ByteBuffer.wrap(dataBytes, 0, 2).order(byteOrder).getShort());
    }

    public Int8 (byte value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public Int8 (short value) throws RangeOutOfBoundsException {
        super(value);
    }

    public Int8 (int value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public Int8 (long value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public Int8 (float value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public Int8 (double value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public Int8 (BigInteger value) throws RangeOutOfBoundsException {
        super(value.shortValue());
    }

    public Int8 (String value) throws RangeOutOfBoundsException {
        super(Short.parseShort(value));
    }

    public void setValue (byte value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    @Override
    public void setValue (Short value) throws RangeOutOfBoundsException {
        this.setValue((short) value);
    }

    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        BigInteger convertedMinimumValue = BigInteger.valueOf(getMinimumValue());
        BigInteger convertedMaximumValue = BigInteger.valueOf(getMaximumValue());

        if (((value.compareTo(convertedMinimumValue) > 0) || (value.equals(convertedMinimumValue))) && ((value.compareTo(convertedMaximumValue) < 0) || (value.equals(convertedMaximumValue)))) {
            this.dataObject = value.shortValue();
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    public void setValue (String value) throws RangeOutOfBoundsException {
        short convertedValue = Short.parseShort(value);

        if ((convertedValue >= getMinimumValue()) && (convertedValue <= getMaximumValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int8 data type is out of bounds!");
        }
    }

    @Override
    public byte[] getAsBytes (ByteOrder byteOrder) {
        byte shortByte = this.dataObject.byteValue();
        byte[] byteAsByteArray;

        {
            byteAsByteArray = (byteOrder != null) ? (ByteBuffer.allocate(1).order(byteOrder).put(shortByte).array()) : (ByteBuffer.allocate(1).put(shortByte).array());
        }

        return byteAsByteArray;
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     * @param int8 The other data object.
     * @return True or False
     */
    public boolean equals (Int8 int8) {
        return Objects.equals(this.dataObject, int8.getValue());
    }

    public static short getByteLength () {
        return 1;
    }

    public static short getMinimumValue () {
        return -128;
    }

    public static short getMaximumValue () {
        return 127;
    }
}