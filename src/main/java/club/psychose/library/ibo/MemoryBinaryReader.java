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

package club.psychose.library.ibo;

import club.psychose.library.ibo.datatypes.types.signed.Int16;
import club.psychose.library.ibo.datatypes.types.signed.Int32;
import club.psychose.library.ibo.datatypes.types.signed.Int64;
import club.psychose.library.ibo.datatypes.types.signed.Int8;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public final class MemoryBinaryReader {
    private ByteBuffer byteBuffer;
    private ByteOrder byteOrder;

    private boolean closed;
    private int offsetPosition;

    /**
     * Default constructor.
     */
    public MemoryBinaryReader () {
        this.byteBuffer = null;
        this.byteOrder = ByteOrder.nativeOrder();

        this.closed = true;
        this.offsetPosition = -1;
    }

    /**
     * Default constructor.
     * @param byteOrder Sets the {@link ByteOrder}
     */
    public MemoryBinaryReader (ByteOrder byteOrder) {
        this.byteBuffer = null;
        this.byteOrder = byteOrder;

        this.closed = true;
        this.offsetPosition = -1;
    }

    /**
     * This method reads the bytes into the ByteBuffer memory and let the user operate with it.
     * @param bytes The byte array
     * @throws OpenedException This exception will be thrown when the BinaryReader is opened but should be closed and the user tries to access it.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (byte[] bytes) throws OpenedException, ClosedException, RangeOutOfBoundsException {
        this.open(bytes, 0);
    }

    /**
     * This method reads the bytes into the ByteBuffer memory and let the user operate with it.
     * @param bytes The byte array
     * @param startOffsetPosition The offset position to start reading.
     * @throws OpenedException This exception will be thrown when the BinaryReader is opened but should be closed and the user tries to access it.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (byte[] bytes, int startOffsetPosition) throws OpenedException, ClosedException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The FileBinaryReader is already opened! - Call close() before using open() again!");

        if ((startOffsetPosition < 0) || (startOffsetPosition > bytes.length))
            throw new RangeOutOfBoundsException("The startOffsetPosition is out of bounds!");

        this.byteBuffer = ByteBuffer.wrap(bytes).order(this.byteOrder);

        this.closed = false;
        this.offsetPosition = 0;

        this.setOffsetPosition(startOffsetPosition);
    }

    /**
     * This method resets and closes the MemoryBinaryReader.
     */
    public void close () {
        if (!(this.isClosed())) {
            this.byteBuffer = null;
            this.closed = true;
            this.offsetPosition = -1;
        }
    }

    /**
     * This method reads the bytes from the memory and return a byte array with the read bytes.
     * @param length The length that should be read.
     * @return Byte array.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public byte[] readBytes (int length) throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((length <= 0) || ((this.offsetPosition + length) > this.getBinaryLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds or the length is negative or 0!");

        int newOffsetPosition = (this.offsetPosition + length);

        byte[] bytes = new byte[length];
        IntStream.range(this.offsetPosition, newOffsetPosition).forEachOrdered(byteIndex -> {
            int index = (this.offsetPosition - byteIndex);
            bytes[index] = this.byteBuffer.get(byteIndex);
        });

        this.setOffsetPosition(newOffsetPosition);
        return bytes;
    }

    /**
     * This method read bytes as an {@link Int8} and returns it.
     * @return {@link Int8}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int8 readInt8 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + Int8.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        int newOffsetPosition = (this.offsetPosition + Int8.getByteLength());
        Int8 int8 = new Int8(this.byteBuffer.get());

        this.setOffsetPosition(newOffsetPosition);
        return int8;
    }

    /**
     * This method read bytes as an {@link UInt8} and returns it.
     * @return {@link UInt8}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt8 readUInt8 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + UInt8.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        int newOffsetPosition = (this.offsetPosition + UInt8.getByteLength());
        UInt8 uInt8 = new UInt8(this.byteBuffer.get());

        this.setOffsetPosition(newOffsetPosition);
        return uInt8;
    }

    /**
     * This method read bytes as an {@link Int16} and returns it.
     * @return {@link Int16}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int16 readInt16 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + Int16.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new Int16(this.readBytes(Int16.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt16} and returns it.
     * @return {@link UInt16}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt16 readUInt16 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + UInt16.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new UInt16(this.readBytes(UInt16.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link Int32} and returns it.
     * @return {@link Int32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 readInt32 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + Int32.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new Int32(this.readBytes(Int32.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt32} and returns it.
     * @return {@link UInt32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt32 readUInt32 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + UInt32.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new UInt32(this.readBytes(UInt32.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link Int64} and returns it.
     * @return {@link Int64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int64 readInt64 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + Int64.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new Int64(this.readBytes(Int64.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt64} and returns it.
     * @return {@link UInt64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 readUInt64 () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((this.offsetPosition + UInt64.getByteLength()) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        return new UInt64(this.readBytes(UInt64.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@code float} and returns it.
     * @return {@code float}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public float readFloat () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        int newOffsetPosition = (this.offsetPosition + Float.BYTES);
        if (newOffsetPosition > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        float binaryFloat = this.byteBuffer.getFloat();

        this.setOffsetPosition(newOffsetPosition);
        return binaryFloat;
    }

    /**
     * This method read bytes as an {@code double} and returns it.
     * @return {@code double}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public double readDouble () throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        int newOffsetPosition = (this.offsetPosition + Double.BYTES);
        if ((this.offsetPosition + Double.BYTES) > this.getBinaryLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        double binaryDouble = this.byteBuffer.getDouble();

        this.setOffsetPosition(newOffsetPosition);
        return binaryDouble;
    }

    /**
     * This method read bytes as an {@code String} and returns it.
     * @param length The length that should be read.
     * @return {@code String}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length) throws ClosedException, RangeOutOfBoundsException {
        return this.readString(length, StandardCharsets.UTF_8);
    }

    /**
     * This method read bytes as an {@code String} and returns it.
     * @param length The length that should be read.
     * @param charset The charset that the string should be encoded to.
     * @return {@code String}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length, Charset charset) throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        int newOffsetPosition = (this.offsetPosition + length);
        if ((length < 0) || (newOffsetPosition > this.getBinaryLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds or the length is negative or 0!");

        byte[] stringBytes = this.readBytes(length);

        this.setOffsetPosition(newOffsetPosition);
        return new String(stringBytes, charset);
    }

    /**
     * This method sets the offset position from the file.
     * @param offsetPosition The offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setOffsetPosition (int offsetPosition) throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        if ((offsetPosition <= 0) || (offsetPosition > this.getBinaryLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        this.offsetPosition = offsetPosition;
        this.byteBuffer.position(offsetPosition);
    }

    /**
     * This method skips a specific offset length from the current offset position.
     * @param length The length that should be skipped.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void skipOffsetPosition (int length) throws ClosedException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        this.setOffsetPosition(this.offsetPosition + length);
    }

    /**
     * This method sets the internal {@link ByteOrder}.
     * @param value The {@link ByteOrder}
     */
    public void setByteOrder (ByteOrder value) {
        this.byteOrder = value;
    }

    /**
     * This method returns the closed state from the reader.
     * @return {@code true} or {@code false}
     */
    public boolean isClosed () {
        return this.closed;
    }

    /**
     * This method returns the internal {@link ByteOrder}.
     * @return {@link ByteOrder}
     */
    public ByteOrder getByteOrder () {
        return this.byteOrder;
    }

    /**
     * This method returns the offset position.
     * @return The offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        return this.offsetPosition;
    }

    /**
     * This method returns the length of the bytes.
     * @return The length of the bytes.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getBinaryLength () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        return this.byteBuffer.capacity();
    }

    /**
     * This method returns the remaining bytes that can be read.
     * @return The amount of the available remaining bytes.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getRemainingBytes () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The MemoryBinaryReader is closed!");

        return this.byteBuffer.remaining();
    }
}