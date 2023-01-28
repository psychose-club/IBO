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

package club.psychose.library.ibo.datatypes.types.signed;

import club.psychose.library.ibo.datatypes.IBODataType;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class handles the Int64 data type.
 */

public final class Int64 extends IBODataType<BigInteger> {
    public Int64 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(new BigInteger(1, ByteBuffer.wrap(dataBytes, 0, 8).array()));
    }

    public Int64 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(new BigInteger(1, ByteBuffer.wrap(dataBytes, 0, 8).order(byteOrder).array()));
    }

    public Int64 (byte value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public Int64 (short value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public Int64 (int value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public Int64 (long value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf(value));
    }

    public Int64 (float value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    public Int64 (double value) throws RangeOutOfBoundsException {
        super(BigInteger.valueOf((long) value));
    }

    public Int64 (BigInteger value) throws RangeOutOfBoundsException {
        super(value);
    }

    public Int64 (String value) throws RangeOutOfBoundsException {
        super(new BigInteger(value));
    }

    public void setValue (byte value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf(value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue().longValue()) && (value <= getMaximumValue().longValue())) {
            this.dataObject = BigInteger.valueOf((long) value);
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
        }
    }

    @Override
    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        if (((value.compareTo(getMinimumValue()) > 0) || (value.equals(getMinimumValue()))) && ((value.compareTo(getMaximumValue()) < 0) || (value.equals(getMaximumValue())))) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int64 data type is out of bounds!");
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

        ByteOrder byteArrayOrder;
        byte[] byteArray;

        {
            ByteBuffer byteBuffer = ByteBuffer.wrap(this.dataObject.toByteArray());
            byteArrayOrder = byteBuffer.order();
            byteArray = byteBuffer.array();
        }

        byte[] buffer = new byte[8];

        // Padding.
        boolean isValueNegative = (this.dataObject.compareTo(BigInteger.valueOf(0)) < 0);
        IntStream.range(0, byteArray.length).forEachOrdered(index -> buffer[index] = (isValueNegative) ? ((byte) 0xFF) : ((byte) 0x0));

        // When the default ByteOrder is different from the required one, we will reverse the bytes.
        if (byteOrder != byteArrayOrder) {
            ArrayList<Byte> byteArrayList = IntStream.range(0, (8 - byteArray.length)).mapToObj(index -> (isValueNegative) ? ((byte) 0xFF) : ((byte) 0x0)).collect(Collectors.toCollection(ArrayList::new));

            for (Byte arrayByte : byteArray) {
                byteArrayList.add(arrayByte);
            }

            Collections.reverse(byteArrayList);
            IntStream.range(0, buffer.length).forEachOrdered(index -> buffer[index] = byteArrayList.get(index));
        } else {
            System.arraycopy(byteArray, 0, buffer, buffer.length - byteArray.length, byteArray.length);
        }

        return buffer;
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     * @param int64 The other data object.
     * @return True or False
     */
    public boolean equals (Int64 int64) {
        return Objects.equals(this.dataObject, int64.getValue());
    }

    public static short getByteLength () {
        return 8;
    }

    public static BigInteger getMinimumValue () {
        return BigInteger.valueOf(-9223372036854775808L);
    }

    public static BigInteger getMaximumValue () {
        return BigInteger.valueOf(9223372036854775807L);
    }
}