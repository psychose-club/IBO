package club.psychose.library.ibo.core.io;

import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

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
    private boolean stayOpen;

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
     * @param value The state if chunks should be used or not.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException This exception will be thrown when the chunk length is not set.
     */
    public void enableChunkUsage (boolean value) throws ClosedException, InvalidFileModeException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if ((value) && (!(this.chunkLengthSet)))
            throw new IOException("You need to set the chunk length before you enabling the usage of chunks!");

        this.chunksUsed = value;
    }

    /**
     * This method sets the {@link ByteBuffer} of the read bytes.
     * @param value The {@link ByteBuffer}.
     */
    public void setByteOrder (ByteOrder value) {
        this.byteOrder = value;

        if (this.byteBuffer != null)
            this.byteBuffer.order(value);
    }

    /**
     * This method sets the usage of the chunk system.
     * @param value The state if chunks should be used or not.
     * @param chunkLength The chunk length that should be used.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void enableChunkUsage (boolean value, int chunkLength) throws ClosedException, InvalidFileModeException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        this.setChunkLength(chunkLength);
        this.chunksUsed = value;
    }

    /**
     * This method loads a specific chunk into the memory.
     * @param chunkNumber The chunk which should be loaded.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void loadChunkIntoTheMemory (int chunkNumber) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        this.loadChunkIntoTheMemory(chunkNumber, 0);
    }

    /**
     * This method loads a specific chunk into the memory.
     * @param chunkNumber The chunk which should be loaded.
     * @param chunkOffsetPosition The chunk offset position from which should be started from.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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
     * This method sets the length of a single chunk.
     * @param chunkLength The chunk length that should be used.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void setChunkLength (int chunkLength) throws ClosedException, InvalidFileModeException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if ((chunkLength <= 0) || (chunkLength > this.getFileLength()))
            throw new RangeOutOfBoundsException("An invalid chunk length was provided!");

        this.chunkLengthSet = true;
        this.chunkLength = chunkLength;
    }

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
     * This method sets the current file offset position to a new one.
     * @param offsetPosition The offset position which should be set to.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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

        if (this.isChunkUsageEnabled())
            this.checkChunk();
    }

    /**
     * This method skips a specified amount of bytes from the current offset position.
     * @param length The length of the bytes that should be skipped.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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
     * @param value The state of the option.
     */
    public void setStayOnOffsetPosition (boolean value) {
        this.stayOnOffsetPosition = value;
    }

    /**
     * This method didn't close the file to access the file faster.<p>
     * Notice: This will be reset when the close function is called and can consume more memory usage.
     * @param value The state of the option.
     */
    public void setStayFileOpen (boolean value) {
        this.stayOpen = value;
    }

    /**
     * This method returns the chunk from a provided offset position.
     * @param offsetPosition The offset position that should be calculated from.
     * @return The calculated chunk. (It is -1 when no chunk length is defined)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
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
     * @param offsetPosition The file offset position.
     * @return The chunk offset position.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException This exception will be thrown when the chunk length is not set.
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
     * @param chunkNumber The chunk to calculate.
     * @return The calculated start offset position from the chunk.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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
     * This method returns the state if chunks are used or not.
     * @return The state if chunks are used.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public boolean isChunkUsageEnabled () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.chunksUsed;
    }

    /**
     * This method returns the state if the {@link BinaryFile} is closed or not.
     * @return The state if it's closed or not.
     */
    public boolean isClosed () {
        return this.closed;
    }

    /**
     * This method returns the state if the file shouldn't be closed.
     * @return The state of the option.
     */
    public boolean isStayFileOpenEnabled () {
        return this.stayOpen;
    }

    /**
     * This method returns the state if the offset position shouldn't be updated after an operation.
     * @return The state of the option.
     */
    public boolean isStayOnOffsetPositionEnabled () {
        return this.stayOnOffsetPosition;
    }

    /**
     * This method returns the current selected and used {@link ByteOrder}.
     * @return The current used {@link ByteOrder}.
     */
    public ByteOrder getByteOrder () {
        return this.byteOrder;
    }

    /**
     * This method returns the current chunk length.
     * @return The current chunk length. (It is -1 when no chunk length is defined)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
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
     * This method returns the current chunk offset position.
     * @return The chunk offset position. (It is -1 when the chunks aren't used)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
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
     * This method returns the current used chunk.
     * @return The current used chunk. (It is -1 when the chunks aren't used)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
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
     * @return The remaining chunk bytes. (It is -1 when the chunks aren't used)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException This exception will be thrown when the chunk usage is not set.
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
     * @return The current {@link FileMode}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public FileMode getFileMode () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.fileMode;
    }

    /**
     * This method returns the length of the current used file.
     * @return The length of the current used file.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public long getFileLength () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.filePath.toFile().length();
    }

    /**
     * This method returns the current file offset position.
     * @return The current file offset position.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public long getFileOffsetPosition () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.offsetPosition;
    }

    /**
     * This method returns the {@link Path} of the current used file.
     * @return The {@link Path} of the current used file.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public Path getFilePath () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.filePath;
    }

/* PROTECTED METHODS. */

    /**
     * This method initializes the {@link ByteBuffer}.
     * @param capacity The maximum amount of bytes that should be used.
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
     * @param length The length to read.
     * @return An array of bytes.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void checkChunk () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the chunk methods in the WRITE mode!");

        if (!(this.chunkLengthSet))
            throw new IOException("You need to set the chunk length before you can update the chunks!");

        // When the capacity is not equals to the chunk length or the offset position is in another chunk, then we will read new bytes to the ByteBuffer.
        if ((this.byteBuffer.capacity() != this.chunkLength) || (this.currentChunk != this.calculateChunk(this.offsetPosition))) {
            long remainingBytes = (this.getFileLength() - offsetPosition);

            if (this.chunkLength >= remainingBytes) {
                this.readIntoTheMemory(this.offsetPosition, (int) remainingBytes);
                return;
            }

            this.loadChunkIntoTheMemory(this.offsetPosition, 0);
            this.chunkOffsetPosition = this.calculateChunkOffsetPosition(this.offsetPosition);
        }
    }

    /**
     * This method loads an entire chunk into the memory.
     * @param chunkStartOffsetPosition The start offset position from the chunk.
     * @param chunkOffsetPosition The chunk offset position from which should be started from.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void loadChunkIntoTheMemory (long chunkStartOffsetPosition, int chunkOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
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
     * @param offsetPosition The offset position where the reading should be started from.
     * @param length The length to read.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
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

        if (!(this.isStayFileOpenEnabled())) {
            String randomAccessFileMode = (this.getFileMode().equals(FileMode.READ)) ? ("r") : ("rw");
            this.randomAccessFile = new RandomAccessFile(this.filePath.toFile(), randomAccessFileMode);
        }

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

            randomAccessFile.seek(offsetPosition);
            randomAccessFile.read(buffer, 0, length);
            randomAccessFile.close();

            this.byteBuffer = ByteBuffer.wrap(buffer).order(this.byteOrder);
            this.byteBuffer.position(0);
        }

        if (!(this.isStayFileOpenEnabled()))
            this.randomAccessFile.close();
    }

    /**
     * This method writes bytes to a file.
     * @param offsetPosition The offset position where should be started from.
     * @param bytes The array of bytes.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    protected void writeBytes (long offsetPosition, byte[] bytes) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.fileMode.equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        if ((offsetPosition < 0) || (offsetPosition > this.getFileLength()))
            throw new RangeOutOfBoundsException("The offset position is out of bounds!");

        if (!(this.isStayFileOpenEnabled())) {
            String randomAccessFileMode = (this.getFileMode().equals(FileMode.WRITE)) ? ("w") : ("rw");
            this.randomAccessFile = new RandomAccessFile(this.filePath.toFile(), randomAccessFileMode);
        }

        // Sets the file offset position.
        this.randomAccessFile.seek(offsetPosition);
        this.randomAccessFile.write(bytes);

        // Set the offset position.
        long newOffsetPosition = (offsetPosition + bytes.length);
        if (!(this.isStayOnOffsetPositionEnabled()))
            this.setOffsetPosition(newOffsetPosition);

        if (!(this.isStayFileOpenEnabled()))
            this.randomAccessFile.close();

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
     * This method sets the closed state.<p>
     * Notice: This method is only accessible for an inherited class.
     * @param value The state of the boolean value.
     */
    protected void setClosed (boolean value) {
        this.closed = value;
    }

    /**
     * This method sets the used {@link FileMode}.<p>
     * Notice: This method is only accessible for an inherited class.
     * @param value The used {@link FileMode}.
     */
    protected void setFileMode (FileMode value) {
        this.fileMode = value;
    }

    /**
     * This method sets the {@link Path} of the current used file.<p>
     * Notice: This method is only accessible for an inherited class.
     * @param filePath The used {@link Path}.
     */
    protected void setFilePath (Path filePath) {
        this.filePath = filePath;
    }

    /**
     * This method returns the current used {@link ByteBuffer}.<p>
     * Notice: This method is only accessible for an inherited class.
     * @return The current used {@link ByteBuffer}.
     */
    protected ByteBuffer getByteBuffer () {
        return this.byteBuffer;
    }
}