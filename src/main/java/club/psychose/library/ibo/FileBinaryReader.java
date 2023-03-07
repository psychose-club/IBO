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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The FileBinaryReader class handles binary data types from a file.<p>
 * For reading a byte array that is already read into the memory look at {@link MemoryBinaryReader}.
 */
public final class FileBinaryReader {
    // NOTE: The chunkLength will be ignored when e.g. the readBytes method called with a higher length than the chunk length!
    // However, we temporarily handle the length then as chunkLength, after the reading we will use the original chunkLength again.
    // You are responsible to make sure that length that will be used in the ByteBuffer causes an out of memory error!
    private final int chunkLength;

    private ByteBuffer byteBuffer;
    private ByteOrder byteOrder;

    private boolean closed;
    private Path filePath;

    private int currentChunk;
    private int chunkOffsetPosition;
    private long offsetPosition;

    /**
     * Default constructor.
     * @param chunkLength Reading file bytes from a specific length.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public FileBinaryReader (int chunkLength) throws RangeOutOfBoundsException {
        if (chunkLength <= 0)
            throw new RangeOutOfBoundsException("The chunk length can't be negative or 0!");

        this.chunkLength = chunkLength;

        this.byteBuffer = null;
        this.byteOrder = ByteOrder.nativeOrder();

        this.closed = true;
        this.filePath = null;

        this.currentChunk = -1;
        this.chunkOffsetPosition = -1;
        this.offsetPosition = -1;
    }

    /**
     * Default constructor.
     * @param chunkLength Reading file bytes from a specific length.
     * @param byteOrder Sets the {@link ByteOrder}
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public FileBinaryReader (int chunkLength, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        if (chunkLength <= 0)
            throw new RangeOutOfBoundsException("The chunk length can't be negative or 0!");

        this.chunkLength = chunkLength;

        this.byteBuffer = null;
        this.byteOrder = byteOrder;

        this.closed = true;
        this.filePath = null;

        this.currentChunk = -1;
        this.chunkOffsetPosition = -1;
        this.offsetPosition = -1;
    }

    /**
     * This method reads the file into the memory and let the user operate with it.
     * @param filePath The path to the file.
     * @throws OpenedException This exception will be thrown when the BinaryReader is opened but should be closed and the user tries to access it.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath) throws OpenedException, ClosedException, IOException, RangeOutOfBoundsException {
        this.open(filePath, 0);
    }

    /**
     * This method reads the file into the memory and let the user operate with it.
     * @param filePath The path to the file.
     * @param startOffsetPosition The offset position to start reading in the chunk.
     * @throws OpenedException This exception will be thrown when the BinaryReader is opened but should be closed and the user tries to access it.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath, int startOffsetPosition) throws OpenedException, ClosedException, IOException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The FileBinaryReader is already opened! - Call close() before using open() again!");

        if (!(Files.exists(filePath)))
            throw new FileNotFoundException("The file \"" + filePath + "\" does not exists!");

        if ((startOffsetPosition < 0) || (startOffsetPosition > filePath.toFile().length()))
            throw new RangeOutOfBoundsException("The startOffsetPosition is out of bounds!");

        this.closed = false;
        this.filePath = filePath;

        this.currentChunk = 0;
        this.chunkOffsetPosition = 0;
        this.offsetPosition = 0;

        this.setOffsetPosition(startOffsetPosition);
    }

    /**
     * This method resets and closes the FileBinaryReader.
     */
    public void close () {
        if (!(this.isClosed())) {
            this.byteBuffer = null;

            this.closed = true;
            this.filePath = null;

            this.currentChunk = -1;
            this.chunkOffsetPosition = -1;
            this.offsetPosition = -1;
        }
    }

    /**
     * This method reads the bytes from the memory and return a byte array with the read bytes.
     * @param length The length that should be read.
     * @return Byte array.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public byte[] readBytes (int length) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((length <= 0) || ((this.offsetPosition + length) > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds or the length is negative or 0!");

        long newOffsetPosition = (this.offsetPosition + length);
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, length);

        long newChunkOffsetPosition = (this.chunkOffsetPosition + length);
        if (this.byteBuffer.position() < newChunkOffsetPosition) {
            this.readChunk(this.offsetPosition, length);
        }

        byte[] bytes = new byte[length];

        for (int index = 0; index < length; index ++) {
            bytes[index] = this.byteBuffer.get(this.chunkOffsetPosition + index); // Get the read byte.
        }

        // We set here the offset position again to read a new chunk if required.
        this.setOffsetPosition(newOffsetPosition);

        return bytes;
    }

    /**
     * This method read bytes as an {@link Int8} and returns it.
     * @return {@link Int8}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int8 readInt8 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Int8.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Int8.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Int8.getByteLength());

        Int8 int8 = new Int8(this.byteBuffer.get());

        this.setOffsetPosition(newOffsetPosition);
        return int8;
    }

    /**
     * This method read bytes as an {@link UInt8} and returns it.
     * @return {@link UInt8}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt8 readUInt8 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + UInt8.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + UInt8.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, UInt8.getByteLength());

        UInt8 uInt8 = new UInt8(this.byteBuffer.get());

        this.setOffsetPosition(newOffsetPosition);
        return uInt8;
    }

    /**
     * This method read bytes as an {@link Int16} and returns it.
     * @return {@link Int16}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int16 readInt16 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Int16.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Int16.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Int16.getByteLength());

        return new Int16(this.readBytes(Int16.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt16} and returns it.
     * @return {@link UInt16}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt16 readUInt16 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + UInt16.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + UInt16.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, UInt16.getByteLength());

        return new UInt16(this.readBytes(UInt16.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link Int32} and returns it.
     * @return {@link Int32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 readInt32 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Int32.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Int32.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Int32.getByteLength());
        
        return new Int32(this.readBytes(Int32.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt32} and returns it.
     * @return {@link UInt32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt32 readUInt32 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + UInt32.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + UInt32.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, UInt32.getByteLength());

        return new UInt32(this.readBytes(UInt32.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link Int64} and returns it.
     * @return {@link Int64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int64 readInt64 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Int64.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Int64.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Int64.getByteLength());

        return new Int64(this.readBytes(Int64.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@link UInt64} and returns it.
     * @return {@link UInt64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 readUInt64 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + UInt64.getByteLength()) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + UInt64.getByteLength());
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, UInt64.getByteLength());

        return new UInt64(this.readBytes(UInt64.getByteLength()), this.byteOrder);
    }

    /**
     * This method read bytes as an {@code float} and returns it.
     * @return {@code float}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public float readFloat () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Float.BYTES) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Float.BYTES);
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Float.BYTES);

        float binaryFloat = this.byteBuffer.getFloat();
        
        this.setOffsetPosition(newOffsetPosition);
        return binaryFloat;
    }

    /**
     * This method read bytes as an {@code double} and returns it.
     * @return {@code double}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public double readDouble () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((this.offsetPosition + Double.BYTES) > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        long newOffsetPosition = (this.offsetPosition + Double.BYTES);
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, Double.BYTES);

        double binaryDouble = this.byteBuffer.getDouble();

        this.setOffsetPosition(newOffsetPosition);
        return binaryDouble;
    }

    /**
     * This method read bytes as an {@code String} and returns it.
     * @param length The length that should be read.
     * @return {@code String}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length) throws ClosedException, IOException, RangeOutOfBoundsException {
        return this.readString(length, StandardCharsets.UTF_8);
    }

    /**
     * This method read bytes as an {@code String} and returns it.
     * @param length The length that should be read.
     * @param charset The charset that the string should be encoded to.
     * @return {@code String}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length, Charset charset) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((length < 0) || ((this.offsetPosition + length) > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds or the length is negative or 0!");

        long newOffsetPosition = (this.offsetPosition + length);
        if (this.currentChunk != this.getChunk(newOffsetPosition))
            this.readChunk(this.offsetPosition, length);

        byte[] stringBytes = this.readBytes(length);

        return new String(stringBytes, charset);
    }

    /**
     * This method reads a chunk into the memory.
     * @param chunkNumber The chunk number.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void readChunkIntoTheMemory (int chunkNumber) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        this.readChunk(((long) chunkNumber * this.chunkLength), this.chunkLength);
    }

    /**
     * This method sets the offset position from the file.
     * @param offsetPosition The offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setOffsetPosition (long offsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((offsetPosition < 0) || (offsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        if ((this.offsetPosition == 0) || (this.currentChunk != this.getChunk(offsetPosition)))
            this.readChunkIntoTheMemory(this.getChunk(offsetPosition));

        this.offsetPosition = offsetPosition;
        this.currentChunk = this.getChunk(offsetPosition);
        this.chunkOffsetPosition = this.getChunkOffsetPosition(offsetPosition);

        this.checkChunkState();

        this.byteBuffer.position(this.chunkOffsetPosition);
    }

    /**
     * This method skips a specific offset length from the current offset position.
     * @param length The length that should be skipped.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void skipOffsetPosition (long length) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        this.setOffsetPosition(this.offsetPosition + length);
    }

    /**
     * This method checks if fewer bytes than the chunk length was read into the memory and tries then to resolve the missing bytes.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    private void checkChunkState () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if (this.byteBuffer.capacity() != this.chunkLength) {
            long remainingBytes = this.getRemainingBytes();

            if ((remainingBytes - this.chunkOffsetPosition) >= this.chunkLength) {
                this.readChunkIntoTheMemory(this.getCurrentChunk());
            } else {
                if (this.chunkLength < remainingBytes) {
                    this.readChunkIntoTheMemory(this.getCurrentChunk());
                } else {
                    this.readChunk(this.offsetPosition, (int) remainingBytes);
                }
            }
        }
    }

    /**
     * This method reads a chunk into the memory.<p>
     * It can bypass the chunk restrictions! However, it's considered as unsafe, so we don't let user access the method.
     * @param offsetPosition The offset position.
     * @param readLength The length that should be read.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    private void readChunk (long offsetPosition, int readLength) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (offsetPosition + readLength);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(this.filePath.toFile(), "r")) {
            byte[] buffer = new byte[readLength];

            randomAccessFile.seek(offsetPosition);
            randomAccessFile.read(buffer, 0, readLength);
            randomAccessFile.close();

            this.byteBuffer = ByteBuffer.wrap(buffer).order(this.byteOrder);
            this.byteBuffer.position(0);
            this.chunkOffsetPosition = 0;

            if (this.currentChunk != this.getChunk(newOffsetPosition))
                this.currentChunk = this.getChunk(newOffsetPosition);
        }
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
     * This method returns the chunk number from an offset position.
     * @param offsetPosition The offset position.
     * @return The chunk number.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunk (long offsetPosition) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if (offsetPosition == 0)
            offsetPosition = 1;

        return (int) (offsetPosition / this.chunkLength);
    }

    /**
     * This method returns the offset position from the current chunk.
     * @return The current chunk offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunkOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.chunkOffsetPosition;
    }

    /**
     * This method calculates the chunk offset position from the file offset position.
     * @param offsetPosition The file offset position.
     * @return The chunk offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunkOffsetPosition (long offsetPosition) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return (int) (offsetPosition % this.chunkLength);
    }

    /**
     * This method returns the current used chunk.
     * @return The current chunk.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getCurrentChunk () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.currentChunk;
    }

    /**
     * This method returns the offset position from the file.
     * @return The file offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getFileOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.offsetPosition;
    }

    /**
     * This method returns the file length of the binary file.
     * @return The file length.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getFileLength () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.filePath.toFile().length();
    }

    /**
     * This method returns the remaining file bytes that can be read.
     * @return The amount of the available remaining file bytes.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getRemainingBytes () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return (this.getFileLength() - this.offsetPosition);
    }

    /**
     * This method returns the remaining chunk bytes that can be read before a new chunk begin.
     * @return The amount of the available remaining chunk bytes.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getRemainingChunkBytes () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return (this.chunkLength - this.getChunkOffsetPosition(this.offsetPosition));
    }
}