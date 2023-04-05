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

package club.psychose.library.ibo.core.io.reader;

import club.psychose.library.ibo.core.datatypes.types.signed.Int16;
import club.psychose.library.ibo.core.datatypes.types.signed.Int32;
import club.psychose.library.ibo.core.datatypes.types.signed.Int64;
import club.psychose.library.ibo.core.datatypes.types.signed.Int8;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.library.ibo.interfaces.ReaderInterface;
import club.psychose.library.ibo.utils.HEXUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

/**
 * The FileBinaryReader class handles binary data types from a file.<p>
 * We will read file bytes as "chunks" to prevent an overflow of the memory.<p>
 * For reading a byte array that is already read into the memory look at {@link MemoryBinaryReader}.<p>
 * WARNING! The chunkLength will be ignored when e.g. the readBytes method called with a higher length than the chunk length!<p>
 * However, we temporarily handle the length then as chunkLength, after the reading we will use the original chunkLength again.<p>
 * You are responsible to make sure that length that will be used in the ByteBuffer not causes an out of memory error!
 */

public final class FileBinaryReader extends ChunkManagement implements ReaderInterface {
    private long offsetPosition;

    /**
     * Default constructor.
     * @param chunkLength Reading file bytes from a specific length.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public FileBinaryReader (int chunkLength) throws RangeOutOfBoundsException {
        super(chunkLength);

        this.offsetPosition = -1;
        this.setCurrentChunk(-1);
        this.setChunkOffsetPosition(-1);
    }

    /**
     * Default constructor.
     * @param chunkLength Reading file bytes from a specific length.
     * @param byteOrder Sets the {@link ByteOrder}
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public FileBinaryReader (int chunkLength, ByteOrder byteOrder) throws RangeOutOfBoundsException {
        super(chunkLength);

        this.offsetPosition = -1;
        this.setCurrentChunk(-1);
        this.setChunkOffsetPosition(-1);
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
        this.open(filePath, 0x0);
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

        this.setClosed(false);
        this.setFilePath(filePath);

        this.offsetPosition = 0;
        this.setCurrentChunk(0);
        this.setChunkOffsetPosition(0);

        this.setOffsetPosition(startOffsetPosition);
    }

    /**
     * This method resets and closes the FileBinaryReader.
     */
    @Override
    public void close() {
        if (!(this.isClosed())) {
            this.resetTheByteBuffer();

            this.setClosed(true);
            this.setFilePath(null);

            this.offsetPosition = -1;
            this.setCurrentChunk(-1);
            this.setChunkOffsetPosition(-1);
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
    @Override
    public byte[] readBytes (int length) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if (length <= 0)
            throw new RangeOutOfBoundsException("The length to read is negative or 0!");

        long newOffsetPosition = (this.offsetPosition + length);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.getCurrentChunk() != this.getChunk(newOffsetPosition))
            this.read(this.offsetPosition, length);

        // Get the bytes from the ByteBuffer.
        byte[] bytes = new byte[length];

        int chunkOffsetPosition = this.getChunkOffsetPosition();
        IntStream.range(0, length).forEach(index -> bytes[index] = this.getByteBuffer().get(chunkOffsetPosition + index));

        this.setChunkOffsetPosition(chunkOffsetPosition + length);
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
    @Override
    public Int8 readInt8 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Int8.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.getCurrentChunk() != this.getChunk(newOffsetPosition))
            this.read(this.offsetPosition, Int8.getByteLength());

        Int8 int8 = new Int8(this.getByteBuffer().get());
        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Int8.getByteLength());
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
    @Override
    public UInt8 readUInt8 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + UInt8.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.getCurrentChunk() != this.getChunk(newOffsetPosition))
            this.read(this.offsetPosition, UInt8.getByteLength());

        UInt8 uInt8 = new UInt8(this.getByteBuffer().get());
        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + UInt8.getByteLength());
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
    @Override
    public Int16 readInt16 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Int16.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new Int16(this.readBytes(Int16.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@link UInt16} and returns it.
     * @return {@link UInt16}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public UInt16 readUInt16 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + UInt16.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new UInt16(this.readBytes(UInt16.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@link Int32} and returns it.
     * @return {@link Int32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public Int32 readInt32 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Int32.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new Int32(this.readBytes(Int32.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@link UInt32} and returns it.
     * @return {@link UInt32}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public UInt32 readUInt32 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + UInt32.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new UInt32(this.readBytes(UInt32.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@link Int64} and returns it.
     * @return {@link Int64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public Int64 readInt64 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Int64.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new Int64(this.readBytes(Int64.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@link UInt64} and returns it.
     * @return {@link UInt64}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public UInt64 readUInt64 () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + UInt64.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        return new UInt64(this.readBytes(UInt64.getByteLength()), this.getByteOrder());
    }

    /**
     * This method read bytes as an {@code float} and returns it.
     * @return {@code float}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
    public float readFloat () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Float.BYTES);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.getCurrentChunk() != this.getChunk(newOffsetPosition))
            this.read(this.offsetPosition, Float.BYTES);

        float binaryFloat = this.getByteBuffer().getFloat();

        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Float.BYTES);
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
    @Override
    public double readDouble () throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + Double.BYTES);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.getCurrentChunk() != this.getChunk(newOffsetPosition))
            this.read(this.offsetPosition, Double.BYTES);

        double binaryDouble = this.getByteBuffer().getDouble();

        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Double.BYTES);
        this.setOffsetPosition(newOffsetPosition);

        return binaryDouble;
    }

    /**
     * This method read bytes as an {@code String} and returns it.<p>
     * The default charset is UTF_8!
     * @param length The length that should be read.
     * @return {@code String}
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    @Override
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
    @Override
    public String readString (int length, Charset charset) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (this.offsetPosition + length);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        byte[] stringBytes = this.readBytes(length);
        return new String(stringBytes, charset);
    }

    /**
     * This method searches the entire chunk for the first offset position where the provided HEX string matches.
     * @param hexValueToSearch The HEX String that should be matched with.
     * @return The chunk offset position or -1 when nothing was found.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public int searchFirstHEXValueInChunk (String hexValueToSearch) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.searchFirstHEXValueInChunk(hexValueToSearch, 0);
    }

    /**
     * This method searches the entire chunk for the first offset position where the provided HEX string matches.
     * @param hexValueToSearch The HEX String that should be matched with.
     * @param chunk The chunk from which should be used.
     * @return The chunk offset position or -1 when nothing was found.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public int searchFirstHEXValueInChunk (String hexValueToSearch, int chunk) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.searchFirstHEXValueInChunk(hexValueToSearch, chunk, 0);
    }

    /**
     * This method searches the entire chunk for the first offset position where the provided HEX string matches.
     * @param hexValueToSearch The HEX String that should be matched with.
     * @param chunk The chunk from which should be used.
     * @param chunkOffsetPosition The chunk offset position from which should be started with.
     * @return The chunk offset position or -1 when nothing was found.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public int searchFirstHEXValueInChunk (String hexValueToSearch, int chunk, int chunkOffsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((hexValueToSearch.length() % 2) != 0)
            throw new IOException("The provided string is not a HEX string!");

        long oldOffsetPosition = this.getFileOffsetPosition();

        this.readChunkIntoTheMemory(chunk, chunkOffsetPosition);

        // Searching for the entered offset.
        int offset;
        {
            byte[] bytes = new byte[this.getByteBuffer().capacity()];
            IntStream.range(0, bytes.length).forEachOrdered(index -> bytes[index] = this.getByteBuffer().get(index));

            String hexString = HEXUtils.convertBytesToHEXString(bytes);
            int offsetIndex = this.getOffsetPositionFromHEXStrings(hexValueToSearch, hexString);

            if (offsetIndex == -1) {
                this.setOffsetPosition(oldOffsetPosition);
                return -1;
            }

            offset = this.getOffsetPositionFromHEXStrings(hexValueToSearch, hexString) + chunkOffsetPosition;
        }

        this.setOffsetPosition(oldOffsetPosition);
        return offset;
    }

    /**
     * This method sets the current offset position to a new one.
     * @param offsetPosition The offset position which should be set to.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setOffsetPosition (long offsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((offsetPosition < 0) || (offsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        this.offsetPosition = offsetPosition;
        this.setChunkOffsetPosition(this.getChunkOffsetPosition(offsetPosition));

        this.checkChunkState(offsetPosition);
        this.getByteBuffer().position(this.getChunkOffsetPosition());
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

        return (this.getChunkLength() - this.getChunkOffsetPosition(this.offsetPosition));
    }

    /**
     * This method returns the file length of the binary file.
     * @return The file length.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getFileOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.offsetPosition;
    }
}