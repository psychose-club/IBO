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

/**
 * This class handles the UInt8 data type.
 */

public final class UInt8 extends IBODataType<Short> {
    public UInt8 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(ByteBuffer.wrap(dataBytes, 0, 1).getShort());
    }

    public UInt8 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(ByteBuffer.wrap(dataBytes, 0, 1).order(byteOrder).getShort());
    }

    public UInt8 (byte value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public UInt8 (short value) throws RangeOutOfBoundsException {
        super(value);
    }

    public UInt8 (int value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public UInt8 (long value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public UInt8 (float value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public UInt8 (double value) throws RangeOutOfBoundsException {
        super((short) value);
    }

    public UInt8 (BigInteger value) throws RangeOutOfBoundsException {
        super(value.shortValue());
    }

    public UInt8 (String value) throws RangeOutOfBoundsException {
        super(Short.parseShort(value));
    }

    public void setValue (byte value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    @Override
    public void setValue (Short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (short) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        BigInteger convertedMinimumValue = BigInteger.valueOf(getMinimumValue());
        BigInteger convertedMaximumValue = BigInteger.valueOf(getMaximumValue());

        if (((value.compareTo(convertedMinimumValue) > 0) || (value.equals(convertedMinimumValue))) && ((value.compareTo(convertedMaximumValue) < 0) || (value.equals(convertedMaximumValue)))) {
            this.dataObject = value.shortValue();
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    public void setValue (String value) throws RangeOutOfBoundsException {
        short convertedValue = Short.parseShort(value);

        if ((convertedValue >= getMinimumValue()) && (convertedValue <= getMaximumValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the UInt8 data type is out of bounds!");
        }
    }

    @Override
    public byte[] getAsBytes (ByteOrder byteOrder) throws RangeOutOfBoundsException {
        byte[] shortBytes;

        {
            shortBytes = (byteOrder != null) ? (ByteBuffer.allocate(2).order(byteOrder).putShort(this.dataObject).array()) : (ByteBuffer.allocate(2).putShort(this.dataObject).array());
        }

        return this.extractBytes(shortBytes, byteOrder, 1, 1, false);
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     * @param uInt8 The other data object.
     * @return True or False
     */
    public boolean equals (UInt8 uInt8) {
        return Objects.equals(this.dataObject, uInt8.getValue());
    }

    public static short getByteLength () {
        return 1;
    }

    public static short getMinimumValue () {
        return 0;
    }

    public static short getMaximumValue () {
        return 255;
    }
}