package club.psychose.library.ibo.core.datatypes.types.unsigned;

import club.psychose.library.ibo.core.datatypes.IBODataType;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * This class handles the UInt24 data type.
 */
public final class UInt24 extends IBODataType<Integer> {
    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first three bytes, more bytes will be thrown away.<p>
     * The default {@link ByteOrder} is the native order.
     * @param dataBytes The bytes that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (byte[] dataBytes) throws RangeOutOfBoundsException {
        super(0);

        if (dataBytes.length < 3)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than three bytes.");

        if (dataBytes.length > 3) {
            byte[] newBytes = new byte[3];

            for (byte index = 0; index < 3; index ++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue(this.shiftToInt32(dataBytes, ByteOrder.nativeOrder()));
    }

    /**
     * The default constructor.<p>
     * Information: The byte array will be only using the first two bytes, more bytes will be thrown away.
     * @param dataBytes The bytes that should be interpreted as {@link UInt24}.
     * @param byteOrder The used {@link ByteOrder}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (byte[] dataBytes, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(0);

        if (dataBytes.length < 3)
            throw new RangeOutOfBoundsException("The dataBytes are shorter than three bytes.");

        if (dataBytes.length > 3) {
            byte[] newBytes = new byte[3];

            for (byte index = 0; index < 3; index ++) {
                newBytes[index] = dataBytes[index];
            }

            dataBytes = newBytes;
        }

        this.setValue(this.shiftToInt32(dataBytes, byteOrder));
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (byte value) throws RangeOutOfBoundsException {
        super((int) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (short value) throws RangeOutOfBoundsException {
        super((int) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (int value) throws RangeOutOfBoundsException {
        super(value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (long value) throws RangeOutOfBoundsException {
        super((int) value);
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (float value) throws RangeOutOfBoundsException {
        super((int) value);
    }

    /**
     * The default constructor.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (double value) throws RangeOutOfBoundsException {
        super((int) value);
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (BigInteger value) throws RangeOutOfBoundsException {
        super(value.intValue());
    }

    /**
     * The default constructor.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt24 (String value) throws RangeOutOfBoundsException {
        super(Integer.parseInt(value));
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (byte value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (int) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (short value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (int) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public void setValue (Integer value) throws RangeOutOfBoundsException {
        this.setValue((int) value);
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (int value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (long value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (int) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (float value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (int) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.<p>
     * Information: The data type didn't handle floating points.<p>
     * So the floating points will be automatically cut from the value.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (double value) throws RangeOutOfBoundsException {
        if ((value >= getMinimumValue()) && (value <= getMaximumValue())) {
            this.dataObject = (int) value;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (BigInteger value) throws RangeOutOfBoundsException {
        BigInteger convertedMinimumValue = BigInteger.valueOf(getMinimumValue());
        BigInteger convertedMaximumValue = BigInteger.valueOf(getMaximumValue());

        if (((value.compareTo(convertedMinimumValue) > 0) || (value.equals(convertedMinimumValue))) && ((value.compareTo(convertedMaximumValue) < 0) || (value.equals(convertedMaximumValue)))) {
            this.dataObject = value.intValue();
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method sets a new value for the data type.
     * @param value The value that should be interpreted as {@link UInt24}.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setValue (String value) throws RangeOutOfBoundsException {
        int convertedValue = Integer.parseInt(value);

        if ((convertedValue >= getMinimumValue()) && (convertedValue <= getMaximumValue())) {
            this.dataObject = convertedValue;
        } else {
            throw new RangeOutOfBoundsException("The value for the Int24 data type is out of bounds!");
        }
    }

    /**
     * This method returns the data type as an array of {@link Byte}.
     * @param byteOrder ByteOrder for the bytes.
     * @return An array of {@link Byte}
     */
    @Override
    public byte[] getAsBytes (ByteOrder byteOrder) throws RangeOutOfBoundsException {
        return this.extractFromInt32(this.getValue(), (byteOrder != null) ? (byteOrder) : (ByteOrder.nativeOrder()));
    }

    /**
     * This function compares the internal data type with another data object from the same type.
     * @param uInt24 The other data object.
     * @return True or False
     */
    public boolean equals (UInt24 uInt24) {
        return Objects.equals(this.dataObject, uInt24.getValue());
    }

    /**
     * This method stores an {@link UInt24} as a normal {@link Integer} (Int32).
     * @param dataBytes The bytes from the {@link UInt24}.
     * @param byteOrder The {@link ByteOrder} that should be used.
     * @return {@link Integer}
     */
    private int shiftToInt32 (byte[] dataBytes, ByteOrder byteOrder) {
        int uInt24 = 0;

        if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
            uInt24 |= dataBytes[0] << 16;
            uInt24 |= (dataBytes[1] & 0xFF) << 8;
            uInt24 |= dataBytes[2] & 0xFF;
        } else {
            uInt24 |= dataBytes[2] << 16;
            uInt24 |= (dataBytes[1] & 0xFF) << 8;
            uInt24 |= dataBytes[0] & 0xFF;
        }

        return uInt24;
    }

    /**
     * This method extracts an {@link Integer} that contains the {@link UInt24} to a {@link Byte} array.
     * @param uInt24 The {@link UInt24}.
     * @param byteOrder The {@link ByteOrder} that should be used.
     * @return An array of {@link Byte}.
     */
    private byte[] extractFromInt32 (int uInt24, ByteOrder byteOrder) {
        byte[] uInt24ByteArray = new byte[3];

        if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
            uInt24ByteArray[0] |= (byte) (uInt24 >> 16);
            uInt24ByteArray[1] |= (byte) (uInt24 >> 8);
            uInt24ByteArray[2] |= (byte) uInt24;
        } else {
            uInt24ByteArray[2] |= (byte) (uInt24 >> 16);
            uInt24ByteArray[1] |= (byte) (uInt24 >> 8);
            uInt24ByteArray[0] |= (byte) uInt24;
        }

        return uInt24ByteArray;
    }

    /**
     * This method returns the length of the data type.
     * @return {@link Short}
     */
    public static short getByteLength () {
        return 3;
    }

    /**
     * This method returns the minimum value for this data type.
     * @return {@link Integer}
     */
    public static int getMinimumValue () {
        return 0;
    }

    /**
     * This method returns the maximum value for this data type.
     * @return {@link Integer}
     */
    public static int getMaximumValue () {
        return 16777215;
    }
}