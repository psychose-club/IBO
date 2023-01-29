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

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * The BinaryWriter writes data types into a file.<p>
 * It's also supporting byte padding.
 */

public final class BinaryWriter {
    private ByteOrder byteOrder;
    private FileOutputStream fileOutputStream;

    private boolean closed;
    private int offsetPosition;

    private boolean chunkPadding;
    private int chunkSize;
    private byte chunkPaddingByte;

    /**
     * Default constructor.
     */
    public BinaryWriter () {
        this.byteOrder = ByteOrder.nativeOrder();
        this.fileOutputStream = null;

        this.closed = true;
        this.offsetPosition = 0;

        this.chunkPadding = false;
        this.chunkSize = 0x0;
        this.chunkPaddingByte = 0x0;
    }

    /**
     * Default constructor.
     * @param byteOrder Sets the {@link ByteOrder}.
     */
    public BinaryWriter (ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.fileOutputStream = null;

        this.closed = true;
        this.offsetPosition = 0;

        this.chunkPadding = false;
        this.chunkSize = 0x0;
        this.chunkPaddingByte = 0x0;
    }

    /**
     * This method opens the file to write in. (If it's not exist it'll be created)
     * @param filePath The path to the file.
     * @throws OpenedException This exception will be thrown when the BinaryWriter is opened but should be closed and the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while initializing the {@link FileOutputStream}.
     */
    public void open (Path filePath) throws OpenedException, IOException {
        if (!(this.isClosed()))
            throw new OpenedException("The BinaryWriter is already opened!");

        if (!(Files.exists(filePath)))
            Files.createFile(filePath);

        this.fileOutputStream = new FileOutputStream(filePath.toFile());
        this.closed = false;
        this.offsetPosition = 0;
    }

    /**
     * This method resets and closes the BinaryWriter.
     * @throws IOException This exception will be thrown when something goes wrong while closing the {@link FileOutputStream}.
     */
    public void close () throws IOException {
        if (!(this.isClosed())) {
            if (this.fileOutputStream.getChannel().isOpen())
                this.fileOutputStream.close();

            this.fileOutputStream = null;
            this.closed = true;
            this.offsetPosition = 0;
        }
    }

    /**
     * This method writes a specific byte with a provided length into the file.
     * @param fillWithByte The byte to fill the file.
     * @param length The length to write.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void fill (byte fillWithByte, int length) throws ClosedException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

        this.fillOnly(fillWithByte, length);
        
        if (this.chunkPadding) {
            int paddingBytes = this.chunkSize - (length % this.chunkSize);
            this.fillOnly(this.chunkPaddingByte, paddingBytes);
        }
    }

    /**
     * This method writes a specific byte with a provided length into the file and ignores the chunk padding if it's enabled.
     * @param fillWithByte The byte to fill the file.
     * @param length The length to write.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void fillOnly (byte fillWithByte, int length) throws ClosedException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

        byte[] fillBytes = new byte[length];
        Arrays.fill(fillBytes, fillWithByte);

        this.fileOutputStream.write(fillBytes, this.offsetPosition, length);
        this.skipOffsetPosition(length);
    }

    /**
     * This method writes the given bytes into the file.
     * @param bytes The byte array to write into the file.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (byte[] bytes) throws ClosedException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

        this.fileOutputStream.write(bytes, this.offsetPosition, bytes.length);
        this.skipOffsetPosition(bytes.length);

        if (this.chunkPadding) {
            int paddingBytes = this.chunkSize - (bytes.length % this.chunkSize);
            this.fillOnly(this.chunkPaddingByte, paddingBytes);
        }
    }

    /**
     * This method converts the {@link Int8} data type into bytes and writes it to the file.
     * @param int8 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (Int8 int8) throws ClosedException, IOException {
        this.write(int8.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link UInt8} data type into bytes and writes it to the file.
     * @param uInt8 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (UInt8 uInt8) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(uInt8.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link Int16} data type into bytes and writes it to the file.
     * @param int16 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (Int16 int16) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(int16.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link UInt16} data type into bytes and writes it to the file.
     * @param uInt16 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (UInt16 uInt16) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(uInt16.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link Int32} data type into bytes and writes it to the file.
     * @param int32 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (Int32 int32) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(int32.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link UInt32} data type into bytes and writes it to the file.
     * @param uInt32 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (UInt32 uInt32) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(uInt32.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link Int64} data type into bytes and writes it to the file.
     * @param int64 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (Int64 int64) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(int64.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@link UInt64} data type into bytes and writes it to the file.
     * @param uInt64 The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (UInt64 uInt64) throws ClosedException, IOException, RangeOutOfBoundsException {
        this.write(uInt64.getAsBytes(this.byteOrder));
    }

    /**
     * This method converts the {@code float} data type into bytes and writes it to the file.
     * @param value The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (float value) throws ClosedException, IOException {
        this.write(ByteBuffer.allocate(Float.BYTES).order(this.byteOrder).putFloat(value).array());
    }

    /**
     * This method converts the {@code double} data type into bytes and writes it to the file.
     * @param value The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (double value) throws ClosedException, IOException {
        this.write(ByteBuffer.allocate(Double.BYTES).order(this.byteOrder).putDouble(value).array());
    }

    /**
     * This method converts the {@code String} data type into bytes and writes it to the file.
     * @param value The provided data type.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (String value) throws ClosedException, IOException {
        this.write(ByteBuffer.allocate(value.length()).order(this.byteOrder).put(value.getBytes()).array());
    }

    /**
     * This method converts the {@code String} data type into bytes and writes it to the file.
     * @param value The provided data type.
     * @param charset The charset that the bytes should be decoded to.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while writing the bytes.
     */
    public void write (String value, Charset charset) throws ClosedException, IOException {
        this.write(ByteBuffer.allocate(value.length()).order(this.byteOrder).put(value.getBytes(charset)).array());
    }

    /**
     * This method sets the current offset position to a specific position.
     * @param offsetPosition The offset position.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     */
    public void setOffsetPosition (int offsetPosition) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

        this.offsetPosition = offsetPosition;
    }

    /**
     * This method skips a specific offset length from the current offset position.
     * @param length The length that should be skipped.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     */
    public void skipOffsetPosition (int length) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

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
     * This method enables the padding in chunks.
     * @param chunkSize The size that the chunk should be.
     * @param paddingByte The byte that should be used as padding.
     */
    public void enableChunkPadding (int chunkSize, byte paddingByte) {
        this.chunkPadding = true;
        this.chunkSize = chunkSize;
        this.chunkPaddingByte = paddingByte;
    }

    /**
     * This method disables the chunk padding.
     */
    public void disableChunkPadding () {
        this.chunkPadding = false;
    }

    /**
     * This method returns the closed state from the writer.
     * @return {@code true} or {@code false}
     */
    public boolean isClosed () {
        return this.closed;
    }

    /**
     * This method returns the state of chunk padding.
     * @return {@code true} or {@code false}
     */
    public boolean isChunkPaddingEnabled () {
        return this.chunkPadding;
    }

    /**
     * This method returns the chunk padding size.
     * @return The chunk padding size.
     */
    public long getChunkSize () {
        return this.chunkSize;
    }

    /**
     * This method returns the current offset position from the BinaryWriter.
     * @return The offset position.
     * @throws ClosedException This exception will be thrown when the BinaryWriter is closed but the user tries to access it.
     */
    public long getOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryWriter is closed!");

        return this.offsetPosition;
    }
}