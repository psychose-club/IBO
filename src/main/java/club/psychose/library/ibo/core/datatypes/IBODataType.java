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

package club.psychose.library.ibo.core.datatypes;

import club.psychose.library.ibo.enums.HEXFormat;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.library.ibo.utils.HEXUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * This class provides the structure to create a number like data type.
 * @param <DataType> The type that handles the object from the new data type.
 */
public abstract class IBODataType<DataType extends Number> {
    protected DataType dataObject;

    public IBODataType (DataType dataObject) throws RangeOutOfBoundsException {
        this.setValue(dataObject);
    }

    /**
     * This method sets a new internal data object.
     * @param value The new internal value.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public abstract void setValue (DataType value) throws RangeOutOfBoundsException;

    /**
     * This method extracts the required bytes from the data type to return it as byte array.<p>
     * The main problem is that the {@link ByteBuffer} didn't support the right extraction method to remove byte padding.<p>
     * As an example when we want to get short bytes but saved them as an Integer we cannot just convert the Integer as an array of bytes we need to remove the padding from it.<p>
     * We check for a positive value with the paddedBytesLength parameter if an occurrences of {@code 0x0} happens and interpret these as padding.<p>
     * For a negative value we will cast the byte to an Integer with the masking of {@code 0xFF} and checking if the Integer contains the HEX string {@code FF} to interpret theses as padding byte.<p>
     * However, it can happen that the original data type has the exact padding byte also as bytes which we want to extract than too.<p>
     * When the padding is not already happened we will reset an internal counter and write the bytes in the {@link ByteBuffer}.
     * @param bytes The bytes to extract.
     * @param byteOrder The ByteOrder how the bytes should be sorted.
     * @param extractLength The length to extract.
     * @param paddedBytesLength The bytes that are padded to the bytes.
     * @param isValueNegative A negative value is using another padding detection algorithm than a positive one.
     * @return Extracted byte array.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected byte[] extractBytes (byte[] bytes, ByteOrder byteOrder, int extractLength, int paddedBytesLength, boolean isValueNegative) throws RangeOutOfBoundsException {
        if (bytes.length < extractLength)
            throw new RangeOutOfBoundsException("Invalid extraction length provided for byte extraction!");

        if (extractLength < paddedBytesLength)
            throw new RangeOutOfBoundsException("Invalid padding byte length provided for byte extraction!");

        ByteBuffer byteBuffer = (byteOrder != null) ? (ByteBuffer.allocate(extractLength).order(byteOrder)) : (ByteBuffer.allocate(extractLength));

        short bytesFound = 0;
        short paddingCount = 0;
        boolean paddingFound = false;
        boolean wasLastByteIndicatedAsPadding = false;

        for (byte extractedByte : bytes) {
            if (bytesFound == extractLength)
                break;

            if (!(paddingFound)) {
                boolean indicatePaddingUsage = (isValueNegative) ? (Integer.toHexString(extractedByte & 0xFF).equalsIgnoreCase("FF")) : (extractedByte == 0x0);

                if (indicatePaddingUsage) {
                    paddingCount ++;

                    if (paddingCount == paddedBytesLength) {
                        paddingFound = true;
                        continue;
                    }

                    wasLastByteIndicatedAsPadding = true;
                    continue;
                }

                // If the byte is not indicated as padding we will reset the padding counter.
                paddingCount = 0;

                if (wasLastByteIndicatedAsPadding) {
                    if (!(isValueNegative)) {
                        byteBuffer.put((byte) 0x0);
                    } else {
                        byteBuffer.put((byte) 0xFF);
                    }

                    wasLastByteIndicatedAsPadding = false;
                }
            }

            byteBuffer.put(extractedByte);
            bytesFound ++;
        }

        return byteBuffer.array();
    }

    /**
     * This method returns the byte array as big endian.<p>
     * When the {@link ByteOrder} is little endian the bytes will be reversed.<p>
     * To be honest the method could also reverse the bytes from little endian to big endian but while Java interpret bytes as big endian we do it too.
     * @param bytes The byte array that should be returned.
     * @param byteOrder The {@link ByteOrder}.
     * @return The byte array.
     */
    protected byte[] getBytesAsBigEndianByteOrder (byte[] bytes, ByteOrder byteOrder) {
        if (byteOrder == null)
            byteOrder = ByteOrder.nativeOrder();

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            ArrayList<Byte> byteArrayList = new ArrayList<>();

            for (Byte arrayByte : bytes) {
                byteArrayList.add(arrayByte);
            }

            Collections.reverse(byteArrayList);
            IntStream.range(0, bytes.length).forEachOrdered(index -> bytes[index] = byteArrayList.get(index));
        }

        return bytes;
    }

    /**
     * This method returns the internal value of the data object.
     * @return The internal data object.
     */
    public DataType getValue () {
        return this.dataObject;
    }

    /**
     * This function converts the data type to a byte array with the correct byte length.<p>
     * As ByteOrder {@code null} will be inserted which means that the native ByteOrder should be used.
     * @return Byte Array
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public byte[] getAsBytes () throws RangeOutOfBoundsException {
        return this.getAsBytes(null);
    }

    /**
     * This function converts the data type to a byte array with the correct byte length.
     * @param byteOrder ByteOrder for the bytes.
     * @return Byte Array
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public abstract byte[] getAsBytes (ByteOrder byteOrder) throws RangeOutOfBoundsException;

    /**
     * Returns a hash code value for the object.
     * @return Hash code value for this object.
     */
    public int hashCode () {
        return this.dataObject.hashCode();
    }

    /**
     * Returns the internal data type that is stored as a number as a string.
     * @return String
     */
    public String getAsString () {
        return String.valueOf(this.dataObject);
    }

    /**
     * This function converts the byte array from the correct data type into a hexadecimal string.
     * @param hexFormat The format how the output should be printed.
     * @return String
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String getAsHEXString (HEXFormat hexFormat) throws RangeOutOfBoundsException {
        return this.getAsHEXString(hexFormat, null);
    }

    /**
     * This function converts the byte array from the correct data type into a hexadecimal string.
     * @param hexFormat The format how the output should be printed.
     * @param byteOrder The ByteOrder for the getAsByte function.
     * @return String
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String getAsHEXString (HEXFormat hexFormat, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        return HEXUtils.convertBytesToHEXString(this.getAsBytes(byteOrder), hexFormat);
    }
}