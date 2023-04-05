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

import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

/**
 * The ChunkManagement class handles the management of the chunk reading of the {@link club.psychose.library.ibo.core.io.reader.FileBinaryReader} class.
 */

class ChunkManagement extends SharedReaderMethods {
    private boolean closed;

    private ByteBuffer byteBuffer;
    private ByteOrder byteOrder;
    private Path filePath;

    private final int chunkLength;

    private int currentChunk;
    private int chunkOffsetPosition;

    /**
     * The default constructor.
     * @param chunkLength This is the chunk length that should be used.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public ChunkManagement (int chunkLength) throws RangeOutOfBoundsException {
        if (chunkLength <= 0)
            throw new RangeOutOfBoundsException("The chunk length can't be negative or 0!");

        this.byteBuffer = null;
        this.byteOrder = null;
        this.filePath = null;
        this.closed = true;
        this.chunkLength = chunkLength;
    }

    /**
     * This method checks if the current byte buffer has the right chunk loaded.
     * @param offsetPosition The offset position which is currently used.
     * @throws ClosedException This exception will be thrown when the BinaryReader or the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void checkChunkState (long offsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        // When the byte buffer is null we will create one.
        if (this.byteBuffer == null) {
            this.readChunkIntoTheMemory(offsetPosition);
            return;
        }

        // When the capacity is not equals to the chunk length or the offset position is in another chunk, then we will read new bytes to the ByteBuffer.
        if ((this.byteBuffer.capacity() != this.chunkLength) || (this.currentChunk != this.getChunk(offsetPosition))) {
            long remainingBytes = (this.getFileLength() - offsetPosition);

            if (this.chunkLength >= remainingBytes) {
                this.read(offsetPosition, (int) remainingBytes);
                return;
            }

            this.readChunkIntoTheMemory(offsetPosition);
            this.chunkOffsetPosition = this.getChunkOffsetPosition(offsetPosition);
        }
    }

    /**
     * This method reads an entire chunk into the memory.
     * @param offsetPosition The offset position from which should be read from.
     * @throws ClosedException This exception will be thrown when the BinaryReader or the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void readChunkIntoTheMemory (long offsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        this.readChunkIntoTheMemory(this.getChunk(offsetPosition));
    }

    /**
     * This method reads an entire chunk into the memory.
     * @param chunkNumber The chunk number indicating from which chunk we should be read from.
     * @throws ClosedException This exception will be thrown when the BinaryReader or the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void readChunkIntoTheMemory (int chunkNumber) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        this.readChunkIntoTheMemory(chunkNumber, 0);
    }

    /**
     * This method reads an entire chunk into the memory.
     * @param chunkNumber The chunk number indicating from which chunk we should be read from.
     * @param chunkOffsetPosition The offset position in the chunk from which should be start from until the end is reached.
     * @throws ClosedException This exception will be thrown when the BinaryReader or the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void readChunkIntoTheMemory (int chunkNumber, int chunkOffsetPosition) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        if ((chunkOffsetPosition < 0x0) || (chunkOffsetPosition >= this.chunkLength))
            throw new RangeOutOfBoundsException("The chunkOffsetPosition is in an invalid range!");

        long chunkStartOffsetPosition = ((long) chunkNumber * this.chunkLength);

        this.read(chunkStartOffsetPosition, (this.chunkLength - chunkOffsetPosition));
        this.currentChunk = chunkNumber;
        this.chunkOffsetPosition = chunkOffsetPosition;
    }

    /**
     * This method reads a provided amount of bytes into the ByteBuffer and ignores the chunk restrictions.
     * @param offsetPosition The offset position from which should be started from.
     * @param length The amount of bytes which should be read.
     * @throws ClosedException This exception will be thrown when the BinaryReader or the BinaryWriter is closed but the user tries to access it.
     * @throws IOException This exception will be thrown when something goes wrong while reading a new chunk.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void read (long offsetPosition, int length) throws ClosedException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        long newOffsetPosition = (offsetPosition + length);

        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(this.filePath.toFile(), "r")) {
            byte[] buffer = new byte[length];

            randomAccessFile.seek(offsetPosition);
            randomAccessFile.read(buffer, 0, length);
            randomAccessFile.close();

            this.byteBuffer = ByteBuffer.wrap(buffer).order(this.byteOrder);
            this.byteBuffer.position(0);
            this.chunkOffsetPosition = 0;
        }
    }

    /**
     * This method sets the current chunk.
     * @param value The chunk number.
     */
    protected void setCurrentChunk (int value) {
        this.currentChunk = value;
    }

    /**
     * This method sets the current chunk offset position.
     * @param value The chunk offset position.
     */
    protected void setChunkOffsetPosition (int value) {
        this.chunkOffsetPosition = value;
    }

    /**
     * This method resets the ByteBuffer.
     */
    protected void resetTheByteBuffer () {
        this.byteBuffer = null;
    }

    /**
     * This method sets the closed state from the reader.
     * @param value The state.
     */
    protected void setClosed (boolean value) {
        this.closed = value;
    }

    /**
     * This method sets the file path to the file from which should be read from.
     * @param value The file path.
     */
    protected void setFilePath (Path value) {
        this.filePath = value;
    }

    /**
     * This method returns the internal ByteBuffer.
     * @return The internal ByteBuffer.
     */
    protected ByteBuffer getByteBuffer () {
        return this.byteBuffer;
    }

    /**
     * This method calculates the chunk number from an offset position.
     * @param offsetPosition The offset position.
     * @return The calculated chunk number.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunk (long offsetPosition) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return (int) (offsetPosition / this.chunkLength);
    }

    /**
     * This method calculates the chunk offset position from an offset position.
     * @param offsetPosition The offset position.
     * @return The calculated chunk offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunkOffsetPosition (long offsetPosition) throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return (int) (offsetPosition % this.chunkLength);
    }

    /**
     * This method sets the internal ByteOrder.
     * @param value The ByteOrder.
     */
    public void setByteOrder (ByteOrder value) {
        this.byteOrder = value;
    }

    /**
     * This method returns the internal ByteOrder.
     * @return The internal ByteOrder.
     */
    public ByteOrder getByteOrder () {
        return this.byteOrder;
    }

    /**
     * This method returns the closed state from the reader.
     * @return {@code true} or {@code false}
     */
    public boolean isClosed () {
        return this.closed;
    }

    /**
     * This method returns the set chunk length.
     * @return The chunk length.
     */
    public int getChunkLength () {
        return this.chunkLength;
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
     * This method returns the current chunk offset position.
     * @return The current chunk offset position.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public int getChunkOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.chunkOffsetPosition;
    }

    /**
     * This method returns the file length.
     * @return The file length.
     * @throws ClosedException This exception will be thrown when the BinaryReader is closed but the user tries to access it.
     */
    public long getFileLength () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The FileBinaryReader is closed!");

        return this.filePath.toFile().length();
    }
}