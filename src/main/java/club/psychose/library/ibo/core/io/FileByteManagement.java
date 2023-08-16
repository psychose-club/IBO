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

package club.psychose.library.ibo.core.io;

import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.stream.IntStream;

/**
 * The FileByteManagement class handles the management of the chunk reading or normal file reading for the {@link BinaryFile}.
 */
class FileByteManagement {
    private ByteBuffer byteBuffer;
    private ByteOrder byteOrder;
    private FileMode fileMode;
    private Path filePath;
    private RandomAccessFile randomAccessFile;
    private boolean closed;
    private boolean chunksUsed;
    private boolean chunkLengthSet;
    private boolean stayOnOffsetPosition;
    private int chunkLength;
    private int currentChunk;
    private int chunkOffsetPosition;
    private long offsetPosition;

    /**
     * This is the constructor of the class.
     */
    public FileByteManagement () {
        this.closed = true;
        this.fileMode = null;
        this.chunksUsed = false;
        this.resetChunkManagement();
    }

    /* PUBLIC METHODS. */

    /**
     * This method sets the usage of the chunk system.
     *
     * @param value The state if chunks should be used or not.
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException              This exception will be thrown when the chunk length is not set.
     */
    public void setChunkUsage (boolean value) throws ClosedException, InvalidFileModeException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if ((value) && (!(this.chunkLengthSet)))
            throw new IOException("You need to set the chunk length before you enabling the usage of chunks!");

        this.chunksUsed = value;
        this.chunkOffsetPosition = (value) ? (this.calculateChunkOffsetPosition(this.offsetPosition)) : (-1);
    }

    /**
     * This method sets the usage of the chunk system.
     *
     * @param value       The state if chunks should be used or not.
     * @param chunkLength The chunk length that should be used.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setChunkUsage (boolean value, int chunkLength) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        this.chunksUsed = value;
        this.setChunkLength(chunkLength);
    }

    /**
     * This method loads a specific chunk into the memory.
     *
     * @param chunkNumber The chunk which should be loaded.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void loadChunkIntoTheMemory (int chunkNumber) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        this.loadChunkIntoTheMemory(chunkNumber, 0);
    }

    /**
     * This method loads a specific chunk into the memory.
     *
     * @param chunkNumber         The chunk which should be loaded.
     * @param chunkOffsetPosition The chunk offset position from which should be started from.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void loadChunkIntoTheMemory (int chunkNumber, int chunkOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        if ((chunkOffsetPosition < 0) || (chunkOffsetPosition > this.chunkLength))
            throw new IOException("The provided chunk offset position is out of bounds!");

        long chunkStartOffsetPosition = this.calculateChunkStartOffsetPosition(chunkNumber);

        if ((chunkStartOffsetPosition + chunkOffsetPosition) > this.getFileLength())
            throw new RangeOutOfBoundsException("The start offset position is out of bounds!");

        this.loadChunkIntoTheMemory(chunkStartOffsetPosition, chunkOffsetPosition);
    }

    /**
     * This method updates the bytes from the current chunk. (Useful when a third-party application accesses the file at the same time)
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void updateChunk () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        this.readIntoTheMemory(this.calculateChunkStartOffsetPosition(this.getCurrentChunk()), this.chunkLength);
    }

    /**
     * This method sets the current file offset position to a new one.
     *
     * @param offsetPosition The offset position which should be set to.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setOffsetPosition (long offsetPosition) throws ClosedException, IOException, InvalidFileModeException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if ((offsetPosition < 0) || (offsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        if (this.isStayOnOffsetPositionEnabled())
            return;

        this.offsetPosition = offsetPosition;

        if (this.isChunkUsageEnabled()) {
            this.checkChunk();
            this.chunkOffsetPosition = this.calculateChunkOffsetPosition(this.offsetPosition);
        }
    }

    /**
     * This method skips a specified number of bytes from the current offset position.
     *
     * @param length The length of the bytes that should be skipped.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void skipOffsetPosition (long length) throws ClosedException, IOException, InvalidFileModeException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        this.setOffsetPosition(this.offsetPosition + length);
    }

    /**
     * This method forces that the offset position wouldn't be updated after reading or writing bytes.<p>
     * Notice: This will be reset when the close function is called and setOffsetPosition or skipOffsetPosition also didn't update their offset position when this option is enabled.
     *
     * @param value The state of the option.
     */
    public void setStayOnOffsetPosition (boolean value) {
        this.stayOnOffsetPosition = value;
    }

    /**
     * This method returns the chunk from a provided offset position.
     *
     * @param offsetPosition The offset position that should be calculated from.
     *
     * @return The calculated chunk. (It is -1 when no chunk length is defined)
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public int calculateChunk (long offsetPosition) throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            return -1;

        return (int) (offsetPosition / this.chunkLength);
    }

    /**
     * This method calculates the chunk offset position from an offset position in a file.
     *
     * @param offsetPosition The file offset position.
     *
     * @return The chunk offset position.
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException              This exception will be thrown when the chunk length is not set.
     */
    public int calculateChunkOffsetPosition (long offsetPosition) throws ClosedException, InvalidFileModeException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        return (int) (offsetPosition % this.chunkLength);
    }

    /**
     * This method calculates the start offset position from a chunk.
     *
     * @param chunkNumber The chunk to calculate.
     *
     * @return The calculated start offset position from the chunk.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public long calculateChunkStartOffsetPosition (int chunkNumber) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        long calculatedOffsetPosition = ((long) chunkNumber * this.chunkLength);

        if ((calculatedOffsetPosition < 0) || (calculatedOffsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The calculated offset position is out of bounds!");

        return calculatedOffsetPosition;
    }

    /**
     * This method searches for the next available offset position from a provided byte sequence / array.<p>
     * Information: If the chunk mode is enabled, the usage of chunks will be ignored!<p>
     * Also, be aware that large file can take up to a few minutes to look through!
     *
     * @param bytes The byte sequence to search.
     *
     * @return The next available offset position or -1 when nothing was found.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public long searchNextByteSequence (byte[] bytes) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = this.getFileOffsetPosition() + bytes.length;
        if (newOffsetPosition > this.getFileLength())
            return -1;

        long oldOffsetPosition = this.getFileOffsetPosition();

        boolean wasChunkUsageEnabled = this.isChunkUsageEnabled();
        if (wasChunkUsageEnabled)
            this.setChunkUsage(false);

        byte[] byteBuffer = new byte[bytes.length];
        long offsetPosition = oldOffsetPosition;
        while (offsetPosition <= (this.getFileLength() - bytes.length)) {
            this.randomAccessFile.seek(offsetPosition);
            this.randomAccessFile.readFully(byteBuffer);

            boolean notFound = IntStream.range(0, bytes.length).anyMatch(byteIndex -> byteBuffer[byteIndex] != bytes[byteIndex]);

            if (!(notFound)) {
                this.setOffsetPosition(oldOffsetPosition);

                if (wasChunkUsageEnabled)
                    this.setChunkUsage(true);

                return offsetPosition;
            }

            offsetPosition++;
        }

        this.setOffsetPosition(oldOffsetPosition);

        if (wasChunkUsageEnabled)
            this.setChunkUsage(true);

        return -1;
    }

    /**
     * This method returns the state if chunks are used or not.
     *
     * @return The state if chunks are used.
     *
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public boolean isChunkUsageEnabled () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.chunksUsed;
    }

    /**
     * This method returns the state if the {@link BinaryFile} is closed or not.
     *
     * @return The state if it's closed or not.
     */
    public boolean isClosed () {
        return this.closed;
    }

    /**
     * This method sets the closed state.<p>
     * Notice: This method is only accessible for an inherited class.
     *
     * @param value The state of the boolean value.
     */
    protected void setClosed (boolean value) {
        this.closed = value;
    }

    /**
     * This method returns the state if the offset position shouldn't be updated after an operation.
     *
     * @return The state of the option.
     */
    public boolean isStayOnOffsetPositionEnabled () {
        return this.stayOnOffsetPosition;
    }

    /**
     * This method returns the current selected and used {@link ByteOrder}.
     *
     * @return The current used {@link ByteOrder}.
     */
    public ByteOrder getByteOrder () {
        return this.byteOrder;
    }

    /**
     * This method sets the {@link ByteBuffer} of the read bytes.
     *
     * @param value The {@link ByteBuffer}.
     */
    public void setByteOrder (ByteOrder value) {
        this.byteOrder = value;

        if (this.byteBuffer != null)
            this.byteBuffer.order(value);
    }

    /**
     * This method returns the current chunk length.
     *
     * @return The current chunk length. (It is -1 when no chunk length is defined)
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public int getChunkLength () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            return -1;

        return this.chunkLength;
    }

    /**
     * This method sets the length of a single chunk.
     *
     * @param chunkLength The chunk length that should be used.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setChunkLength (int chunkLength) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if ((chunkLength <= 0) || (chunkLength > this.getFileLength()))
            throw new RangeOutOfBoundsException("An invalid chunk length was provided!");

        this.chunkLengthSet = true;
        this.chunkLength = chunkLength;

        if (this.chunksUsed)
            this.chunkOffsetPosition = this.calculateChunkOffsetPosition(this.offsetPosition);
    }

    /**
     * This method returns the current chunk offset position.
     *
     * @return The chunk offset position. (It is -1 when the chunks aren't used)
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public int getChunkOffsetPosition () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            return -1;

        return this.chunkOffsetPosition;
    }

    /**
     * This method sets the current chunk-offset position.
     *
     * @param chunkOffsetPosition The chunk-offset position.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setChunkOffsetPosition (int chunkOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        if (chunkOffsetPosition > this.chunkLength)
            throw new RangeOutOfBoundsException("The chunk offset position is greater than the chunk length!");

        long fileOffsetPosition = (this.calculateChunk(this.offsetPosition) + chunkOffsetPosition);

        if ((fileOffsetPosition < 0) || (fileOffsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        this.setOffsetPosition(fileOffsetPosition);
        this.chunkOffsetPosition = chunkOffsetPosition;
    }

    /**
     * This method returns the current used chunk.
     *
     * @return The current used chunk. (It is -1 when the chunks aren't used)
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public int getCurrentChunk () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            return -1;

        return this.currentChunk;
    }

    /**
     * This method returns the remaining chunk bytes.
     *
     * @return The remaining chunk bytes. (It is -1 when the chunks aren't used)
     *
     * @throws ClosedException          This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException              This exception will be thrown when the chunk usage is not set.
     */
    public int getRemainingChunkBytes () throws ClosedException, InvalidFileModeException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            return -1;

        return (this.getChunkLength() - this.calculateChunkOffsetPosition(this.offsetPosition));
    }

    /**
     * This method returns the current selected {@link FileMode}.
     *
     * @return The current {@link FileMode}.
     *
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public FileMode getFileMode () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.fileMode;
    }

    /**
     * This method sets the used {@link FileMode}.<p>
     * Notice: This method is only accessible for an inherited class.
     *
     * @param value The used {@link FileMode}.
     */
    protected void setFileMode (FileMode value) {
        this.fileMode = value;
    }

    /**
     * This method returns the length of the current used file.
     *
     * @return The length of the current used file.
     *
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public long getFileLength () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.filePath.toFile().length();
    }

    /* PROTECTED METHODS. */

    /**
     * This method returns the current file-offset position.
     *
     * @return The current file offset position.
     *
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public long getFileOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.offsetPosition;
    }

    /**
     * This method returns the {@link Path} of the current used file.
     *
     * @return The {@link Path} of the current used file.
     *
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public Path getFilePath () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.filePath;
    }

    /**
     * This method sets the {@link Path} of the current used file.<p>
     * Notice: This method is only accessible for an inherited class.
     *
     * @param filePath The used {@link Path}.
     */
    protected void setFilePath (Path filePath) {
        this.filePath = filePath;
    }

    /**
     * This method initializes the {@link ByteBuffer}.
     *
     * @param capacity The maximum number of bytes that should be used.
     */
    protected void initializeByteBuffer (int capacity) {
        this.byteBuffer = ByteBuffer.allocate(capacity).order(this.byteOrder);
    }

    /**
     * This method resets the chunk usage without the fear of exceptions.<p>
     * Notice: This method is only accessible for an inherited class.
     */
    protected void resetChunkUsage () {
        this.chunksUsed = false;
    }

    /**
     * This method resets the offset position without the fear of exceptions.<p>
     * Notice: This method is only accessible for an inherited class.
     */
    protected void resetOffsetPosition () {
        this.offsetPosition = -1;
    }

    /**
     * This method resets the {@link RandomAccessFile}.<p>
     * Notice: This method is only accessible for an inherited class.
     */
    protected void resetRandomAccessFile () {
        try {
            if (this.randomAccessFile.getFD().valid()) {
                this.randomAccessFile.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace(); // <- This really shouldn't happen. | Make sure to close the file before you want to edit it again!
        }

        this.randomAccessFile = null;
    }

    /**
     * This method resets the used options from this class.<p>
     * Notice: This method is only accessible for an inherited class.
     */
    protected void resetChunkManagement () {
        this.byteBuffer = null;
        this.chunkLength = -1;
        this.currentChunk = -1;
        this.chunkOffsetPosition = -1;
        this.offsetPosition = -1;
        this.chunkLengthSet = false;
    }

    /**
     * This method reads bytes from a file into the memory and returns the bytes as an array.
     *
     * @param length The length to read.
     *
     * @return An array of bytes.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected byte[] readBytesFromFile (int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if ((this.offsetPosition + length) > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        this.readIntoTheMemory(this.offsetPosition, length);
        byte[] bytes = this.byteBuffer.array();

        this.byteBuffer = null;
        return bytes;
    }

    /**
     * This method checks the current chunk state.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void checkChunk () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        if (this.currentChunk == -1)
            this.currentChunk = this.calculateChunk(this.offsetPosition);

        // When the capacity is not equals to the chunk length or the offset position is in another chunk, then we will read new bytes to the ByteBuffer.
        if ((this.byteBuffer.capacity() != this.chunkLength) || (this.currentChunk != this.calculateChunk(this.offsetPosition))) {
            long remainingBytes = (this.getFileLength() - offsetPosition);

            if (this.chunkLength >= remainingBytes) {
                this.readIntoTheMemory(this.offsetPosition, (int) remainingBytes);
                return;
            }

            this.loadChunkIntoTheMemory(this.calculateChunkStartOffsetPosition(this.calculateChunk(this.offsetPosition)), 0);
            this.chunkOffsetPosition = this.calculateChunkOffsetPosition(this.offsetPosition);
        }
    }

    /**
     * This method loads an entire chunk into the memory.
     *
     * @param chunkStartOffsetPosition The start offset position from the chunk.
     * @param chunkOffsetPosition      The chunk offset position from which should be started from.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void loadChunkIntoTheMemory (long chunkStartOffsetPosition,
                                           int chunkOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.isChunkUsageEnabled()))
            throw new IOException("You need to enable the chunk usage before you can use the chunk methods!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        if ((chunkOffsetPosition < 0) || (chunkOffsetPosition > this.chunkLength))
            throw new IOException("The provided chunk offset position is out of bounds!");

        this.readIntoTheMemory((chunkStartOffsetPosition + chunkOffsetPosition), (this.chunkLength - chunkOffsetPosition));
        this.chunkOffsetPosition = chunkOffsetPosition;
    }

    /**
     * This method reads the bytes from the file into the memory.
     *
     * @param offsetPosition The offset position where the reading should be started from.
     * @param length         The length to read.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void readIntoTheMemory (long offsetPosition, int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        long newOffsetPosition = (offsetPosition + length);

        if ((newOffsetPosition < 0) || (newOffsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        // Sets the file offset position.
        this.randomAccessFile.seek(offsetPosition);

        // Initializes the ByteBuffer if it's not initialized or when the capacity is not equal to the length to read.
        if ((this.byteBuffer == null) || (this.byteBuffer.capacity() != length)) {
            if (this.byteBuffer != null)
                this.byteBuffer = null;

            this.initializeByteBuffer(length);
        }

        // Read the bytes into the memory.
        {
            byte[] buffer = new byte[length];

            this.randomAccessFile.seek(offsetPosition);
            this.randomAccessFile.read(buffer, 0, length);
            this.randomAccessFile.getFD().sync();

            this.byteBuffer = ByteBuffer.wrap(buffer).order(this.byteOrder);
            this.byteBuffer.position(0);
        }
    }

    /**
     * This method opens the random access file.
     *
     * @throws ClosedException       This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws FileNotFoundException This exception will be thrown when the required file was not found.
     */
    protected void openRandomAccessFile () throws ClosedException, FileNotFoundException {
        this.randomAccessFile = new RandomAccessFile(this.filePath.toFile(), (this.getFileMode().equals(FileMode.READ)) ? ("r") : ("rw"));
    }

    /**
     * This method writes bytes to a file.
     *
     * @param offsetPosition The offset position where should be started from.
     * @param bytes          The array of bytes.
     *
     * @throws ClosedException           This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException  This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void writeBytes (long offsetPosition, byte[] bytes) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        if ((offsetPosition < 0) || (offsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        // Sets the file offset position.
        this.randomAccessFile.seek(offsetPosition);
        this.randomAccessFile.write(bytes);

        // Set the offset position.
        long newOffsetPosition = (offsetPosition + bytes.length);
        if (!(this.isStayOnOffsetPositionEnabled()))
            this.setOffsetPosition(newOffsetPosition);

        this.randomAccessFile.getFD().sync();

        // Should be automatically READ_AND_WRITE, but we will check it for safety reasons again.
        if ((this.isChunkUsageEnabled()) && (this.getFileMode().equals(FileMode.READ_AND_WRITE))) {
            if (this.getByteBuffer() == null)
                return;

            if (!(this.isStayOnOffsetPositionEnabled()))
                return;

            int offsetPositionChunkNumber = this.calculateChunk(newOffsetPosition);

            if (offsetPositionChunkNumber == this.getCurrentChunk()) {
                this.updateChunk();
            } else {
                this.byteBuffer = null;
                this.chunkOffsetPosition = this.calculateChunkOffsetPosition(newOffsetPosition);
            }
        }
    }

    /**
     * This method returns the current used {@link ByteBuffer}.<p>
     * Notice: This method is only accessible for an inherited class.
     *
     * @return The current used {@link ByteBuffer}.
     */
    protected ByteBuffer getByteBuffer () {
        return this.byteBuffer;
    }
}