package club.psychose.library.ibo.core.io;

import club.psychose.library.ibo.core.datatypes.types.signed.Int16;
import club.psychose.library.ibo.core.datatypes.types.signed.Int32;
import club.psychose.library.ibo.core.datatypes.types.signed.Int64;
import club.psychose.library.ibo.core.datatypes.types.signed.Int8;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * This class provides the ability to modify binary files.<p>
 * Files can be accessed in READ, WRITE and READ_AND_WRITE mode.<p>
 * Also, the usage of chunks are supported to reduce memory consumption.<p>
 * Information: If you read something larger in the chunk mode than the chunk itself the missing bytes that are required for the read process will be read into the memory with the already read chunk.
 */

public final class BinaryFile extends FileByteManagement {
    private boolean paddingEnabled;
    private byte paddingByte;
    private int paddingChunkLength;
    
    /**
     * The default constructor.
     */
    public BinaryFile () {
        this.setByteOrder(ByteOrder.nativeOrder());
        this.close();
    }

    /**
     * The default constructor.
     * @param byteOrder Sets the default {@link ByteOrder} for the bytes.
     */
    public BinaryFile (ByteOrder byteOrder) {
        this.setByteOrder(byteOrder);
        this.close();
    }

    /**
     * This method opens the binary file.<p>
     * The default {@link FileMode} is READ_AND_WRITE.
     * @param filePath The file path to the binary file.
     * @param startFromLastOffsetPosition When true, the offset position will be set to the last byte of the file instead of the first byte.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws OpenedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's opened.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath, boolean startFromLastOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The BinaryFile is already opened!");

        this.open(filePath, (startFromLastOffsetPosition) ? (this.getFileLength()) : (0x0), FileMode.READ_AND_WRITE);
    }

    /**
     * This method opens the binary file.
     * @param filePath The file path to the binary file.
     * @param startFromLastOffsetPosition When true, the offset position will be set to the last byte of the file instead of the first byte.
     * @param fileMode The {@link FileMode} that should be used.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws OpenedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's opened.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath, boolean startFromLastOffsetPosition, FileMode fileMode) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The BinaryFile is already opened!");

        this.open(filePath, (startFromLastOffsetPosition) ? (this.getFileLength()) : (0x0), fileMode);
    }

    /**
     * This method opens the binary file.
     * The default {@link FileMode} is READ_AND_WRITE.
     * @param filePath The file path to the binary file.
     * @param startOffsetPosition The file offset position that should be started from.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws OpenedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's opened.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath, long startOffsetPosition) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The BinaryFile is already opened!");

        this.open(filePath, startOffsetPosition, FileMode.READ_AND_WRITE);
    }

    /**
     * This method opens the binary file.
     * @param filePath The file path to the binary file.
     * @param startOffsetPosition The file offset position that should be started from.
     * @param fileMode The {@link FileMode} that should be used.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws OpenedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's opened.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void open (Path filePath, long startOffsetPosition, FileMode fileMode) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        if (!(this.isClosed()))
            throw new OpenedException("The BinaryFile is already opened!");

        if (!(Files.exists(filePath)))
            throw new FileNotFoundException("The file \"" + filePath + "\" does not exists!");

        if ((startOffsetPosition < 0) || (startOffsetPosition > filePath.toFile().length()))
            throw new RangeOutOfBoundsException("The startOffsetPosition is out of bounds!");

        this.setClosed(false);
        this.setFileMode(fileMode);
        this.setFilePath(filePath);
        this.setByteOrder(this.getByteOrder()); // Sets the ByteBuffer ByteOrder.

        this.openRandomAccessFile();
        this.setOffsetPosition(startOffsetPosition);
    }

    /**
     * This method closes the {@link BinaryFile} and resets the used options.
     */
    public void close () {
        if (!(this.isClosed())) {
            this.paddingEnabled = false;
            this.paddingByte = 0x0;
            this.paddingChunkLength = 0x0;

            this.setFileMode(null);
            this.setFilePath(null);
            this.resetRandomAccessFile();

            this.setClosed(true);
            this.resetChunkUsage();
            this.resetChunkManagement();
            this.setStayOnOffsetPosition(false);

            this.resetOffsetPosition();
        }
    }

    /**
     * This method reads bytes from the file and returns it.
     * @param length The length that should be read.
     * @return An array of bytes.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public byte[] readBytes (int length) throws ClosedException, InvalidFileModeException, RangeOutOfBoundsException, IOException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        if (length <= 0)
            throw new RangeOutOfBoundsException("The length to read can't be negative or 0!");

        long newOffsetPosition = (this.getFileOffsetPosition() + length);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(length);
            this.skipOffsetPosition(length);
            return bytes;
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.calculateChunkStartOffsetPosition(this.calculateChunk(this.getFileOffsetPosition())), this.getChunkLength() + length);

        byte[] bytes = new byte[length];
        for (int bufferIndex = 0; bufferIndex < length; bufferIndex ++) {
            bytes[bufferIndex] = this.getByteBuffer().get(this.getChunkOffsetPosition() + bufferIndex);
        }

        this.skipOffsetPosition(length);
        this.setChunkOffsetPosition(this.calculateChunkOffsetPosition(newOffsetPosition));
        return bytes;
    }

    /**
     * This method reads a byte from a file as {@link Int8}.
     * @return {@link Int8}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int8 readInt8 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Int8.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(Int8.getByteLength());
            this.skipOffsetPosition(Int8.getByteLength());
            return new Int8(bytes);
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), Int8.getByteLength());

        Int8 int8 = new Int8(this.getByteBuffer().get());
        this.skipOffsetPosition(Int8.getByteLength());
        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Int8.getByteLength());

        return int8;
    }

    /**
     * This method reads a byte from a file as {@link UInt8}.
     * @return {@link UInt8}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt8 readUInt8 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + UInt8.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(UInt8.getByteLength());
            this.skipOffsetPosition(UInt8.getByteLength());
            return new UInt8(bytes);
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), UInt8.getByteLength());

        UInt8 uInt8 = new UInt8(this.getByteBuffer().get());
        this.skipOffsetPosition(UInt8.getByteLength());
        this.setChunkOffsetPosition(this.getChunkOffsetPosition() + UInt8.getByteLength());

        return uInt8;
    }

    /**
     * This method reads bytes from a file as {@link Int16}.
     * @return {@link Int16}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int16 readInt16 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Int16.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(Int16.getByteLength());
            this.skipOffsetPosition(Int16.getByteLength());
            return new Int16(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), Int16.getByteLength());

        return new Int16(this.readBytes(Int16.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link UInt16}.
     * @return {@link UInt16}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt16 readUInt16 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + UInt16.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(UInt16.getByteLength());
            this.skipOffsetPosition(UInt16.getByteLength());
            return new UInt16(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), UInt16.getByteLength());

        return new UInt16(this.readBytes(UInt16.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link Int32}.
     * @return {@link Int32}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int32 readInt32 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Int32.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(Int32.getByteLength());
            this.skipOffsetPosition(Int32.getByteLength());
            return new Int32(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), Int32.getByteLength());

        return new Int32(this.readBytes(Int32.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link UInt32}.
     * @return {@link UInt32}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt32 readUInt32 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + UInt32.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(UInt32.getByteLength());
            this.skipOffsetPosition(UInt32.getByteLength());
            return new UInt32(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), UInt32.getByteLength());

        return new UInt32(this.readBytes(UInt32.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link Int64}.
     * @return {@link Int64}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public Int64 readInt64 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Int64.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(Int64.getByteLength());
            this.skipOffsetPosition(Int64.getByteLength());
            return new Int64(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), Int64.getByteLength());

        return new Int64(this.readBytes(Int64.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link UInt64}.
     * @return {@link UInt64}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public UInt64 readUInt64 () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + UInt64.getByteLength());
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (!(this.isChunkUsageEnabled())) {
            byte[] bytes = this.readBytesFromFile(UInt64.getByteLength());
            this.skipOffsetPosition(UInt64.getByteLength());
            return new UInt64(bytes, this.getByteOrder());
        }

        if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
            this.readIntoTheMemory(this.getFileOffsetPosition(), UInt64.getByteLength());

        return new UInt64(this.readBytes(UInt64.getByteLength()), this.getByteOrder());
    }

    /**
     * This method reads bytes from a file as {@link Float}.
     * @return {@link Float}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public float readFloat () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Float.BYTES);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.isChunkUsageEnabled()) {
            if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
                this.readIntoTheMemory(this.getFileOffsetPosition(), Float.BYTES);
        }

        this.readIntoTheMemory(this.getFileOffsetPosition(), Float.BYTES);
        this.skipOffsetPosition(Float.BYTES);

        if (this.isChunkUsageEnabled())
            this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Float.BYTES);

        return this.getByteBuffer().getFloat();
    }

    /**
     * This method reads bytes from a file as {@link Double}.
     * @return {@link Double}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public double readDouble () throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + Double.BYTES);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        if (this.isChunkUsageEnabled()) {
            if (this.getCurrentChunk() != this.calculateChunk(newOffsetPosition))
                this.readIntoTheMemory(this.getFileOffsetPosition(), Double.BYTES);
        }

        this.readIntoTheMemory(this.getFileOffsetPosition(), Double.BYTES);
        this.skipOffsetPosition(Double.BYTES);

        if (this.isChunkUsageEnabled())
            this.setChunkOffsetPosition(this.getChunkOffsetPosition() + Double.BYTES);

        return this.getByteBuffer().getDouble();
    }

    /**
     * This method reads bytes from a file as {@link String}. Information: The charset used here is UTF-8!
     * @param length The length that should be read.
     * @return {@link String}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        return this.readString(length, StandardCharsets.UTF_8);
    }

    /**
     * This method reads bytes from a file as {@link String}.
     * @param length The length that should be read.
     * @param charset The charset that should be used for the {@link String}
     * @return {@link String}
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public String readString (int length, Charset charset) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        long newOffsetPosition = (this.getFileOffsetPosition() + length);
        if (newOffsetPosition > this.getFileLength())
            throw new RangeOutOfBoundsException("The new offset position is out of bounds!");

        byte[] stringBytes = this.readBytes(length);
        return new String(stringBytes, charset);
    }

    /**
     * This method fills bytes from a specific length.
     * @param fillWithByte The byte to fill.
     * @param length The length of the filling process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void fill (byte fillWithByte, int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        this.fill(this.getFileOffsetPosition(), fillWithByte, length);
    }

    /**
     * This method fills bytes from a specific length.
     * @param offsetPositionToStartFrom The offset position from which should be started from to write.
     * @param fillWithByte The byte to fill.
     * @param length The length of the filling process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void fill (long offsetPositionToStartFrom, byte fillWithByte, int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        if ((offsetPositionToStartFrom < 0) || (offsetPositionToStartFrom > this.getFileLength()))
            throw new RangeOutOfBoundsException("The provided offset position is out of bounds!");

        if (length <= 0)
            throw new RangeOutOfBoundsException("The fill length is out of bounds!");

        this.fillWithoutPadding(fillWithByte, length);

        if (this.isPaddingEnabled()) {
            int paddingLength = (this.paddingChunkLength - (length % this.paddingChunkLength));
            this.fillWithoutPadding(this.paddingByte, paddingLength);
        }
    }

    /**
     * This method fills bytes from a specific length without the usage of padding if it's enabled.
     * @param fillWithByte The byte to fill.
     * @param length The length of the filling process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void fillWithoutPadding (byte fillWithByte, int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        this.fillWithoutPadding(this.getFileOffsetPosition(), fillWithByte, length);
    }

    /**
     * This method fills bytes from a specific length without the usage of padding if it's enabled.
     * @param offsetPositionToStartFrom The offset position from which should be started from to write.
     * @param fillWithByte The byte to fill.
     * @param length The length of the filling process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void fillWithoutPadding (long offsetPositionToStartFrom, byte fillWithByte, int length) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        if ((offsetPositionToStartFrom < 0) || (offsetPositionToStartFrom > this.getFileLength()))
            throw new RangeOutOfBoundsException("The provided offset position is out of bounds!");

        if (length <= 0)
            throw new RangeOutOfBoundsException("The fill length is out of bounds!");

        byte[] fillBytes = new byte[length];
        Arrays.fill(fillBytes, fillWithByte);

        this.writeBytes(offsetPositionToStartFrom, fillBytes);
    }

    /**
     * This method writes the given array of {@link Byte} into the file.
     * @param bytes The bytes that should be written.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (byte[] bytes) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.writeBytes(this.getFileOffsetPosition(), bytes);

        if (this.isPaddingEnabled()) {
            int paddingLength = (this.paddingChunkLength - (bytes.length % this.paddingChunkLength));
            this.fillWithoutPadding(this.paddingByte, paddingLength);
        }
    }

    /**
     * This method writes an {@link Int8} into a file.
     * @param int8 The {@link Int8}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (Int8 int8) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(int8.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link UInt8} into a file.
     * @param uInt8 The {@link UInt8}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (UInt8 uInt8) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(uInt8.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link Int16} into a file.
     * @param int16 The {@link Int16}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (Int16 int16) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(int16.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link UInt16} into a file.
     * @param uInt16 The {@link UInt16}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (UInt16 uInt16) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(uInt16.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link Int32} into a file.
     * @param int32 The {@link Int32}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (Int32 int32) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(int32.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link UInt32} into a file.
     * @param uInt32 The {@link UInt32}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (UInt32 uInt32) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(uInt32.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link Int64} into a file.
     * @param int64 The {@link Int64}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (Int64 int64) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(int64.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link UInt64} into a file.
     * @param uInt64 The {@link UInt64}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (UInt64 uInt64) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(uInt64.getAsBytes(this.getByteOrder()));
    }

    /**
     * This method writes an {@link Byte} into a file.
     * @param byteValue The {@link Byte}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (byte byteValue) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(ByteBuffer.allocate(Byte.BYTES).order(this.getByteOrder()).put(byteValue).array());
    }

    /**
     * This method writes an {@link Float} into a file.
     * @param floatValue The {@link Float}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (float floatValue) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(ByteBuffer.allocate(Float.BYTES).order(this.getByteOrder()).putFloat(floatValue).array());
    }

    /**
     * This method writes an {@link Double} into a file.
     * @param doubleValue The {@link Double}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (double doubleValue) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(ByteBuffer.allocate(Double.BYTES).order(this.getByteOrder()).putDouble(doubleValue).array());
    }

    /**
     * This method writes an {@link String} into a file. Information: The charset used here is UTF-8!
     * @param stringValue The {@link String}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (String stringValue) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(stringValue, StandardCharsets.UTF_8);
    }

    /**
     * This method writes an {@link String} into a file. Information: The charset used here is UTF-8!
     * @param stringValue The {@link String}.
     * @param charset The {@link Charset}.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public void write (String stringValue, Charset charset) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.write(ByteBuffer.allocate(stringValue.length()).order(this.getByteOrder()).put(stringValue.getBytes(charset)).array());
    }

    /**
     * This method enables the padding for the writing process.<p>
     * The padding processes uses a chunk system to understand it better here are some examples:<p>
     * {@code Padding Chunk Length: 20} & {@code Fill length: 15} = {@code Padding Length: 5}<p>
     * {@code Padding Chunk Length: 20} & {@code Fill length: 21} = {@code Padding Length: 19}<p>
     * {@code Padding Chunk Length: 13} & {@code Fill length: 56} = {@code Padding Length: 9}<p>
     * @param paddingChunkLength The padding length of the file chunks.
     * @param paddingByte The byte that should be used for the padding process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public void enablePadding (int paddingChunkLength, byte paddingByte) throws ClosedException, InvalidFileModeException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        if (paddingChunkLength <= 0)
            throw new RangeOutOfBoundsException("The padding length is out of bounds!");

        this.paddingEnabled = true;
        this.paddingByte = paddingByte;
        this.paddingChunkLength = paddingChunkLength;
    }

    /**
     * This method disables the padding for the writing process.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public void disablePadding () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        this.paddingEnabled = false;
        this.paddingByte = 0x0;
        this.paddingChunkLength = 0x0;
    }

    /**
     * This method searches for the next available offset position from a specific HEX value.
     * Information: If the chunk mode is enabled, the usage of chunks will be ignored!
     * @param hexValue The HEX string.
     * @return The next available offset position or -1 when nothing was found.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public long searchNextHEXValue (String hexValue) throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.READ))
            throw new InvalidFileModeException("Insufficient permissions to access the read methods in the WRITE mode!");

        if ((hexValue.length() % 2) != 0)
            throw new RangeOutOfBoundsException("The HEX value string is not a valid HEX string!");

        // Converts the HEX string into their byte sequence.
        byte[] hexBytes = new byte[hexValue.length() / 2];

        for (int index = 0; index < hexValue.length(); index += 2) {
            hexBytes[index / 2] = (byte) ((Character.digit(hexValue.charAt(index), 16) << 4) + Character.digit(hexValue.charAt(index + 1), 16));
        }

        return this.searchNextByteSequence(hexBytes);
    }

    /**
     * This method searches for the next available offset position from a provided byte sequence / array.<p>
     * Information: If the chunk mode is enabled, the usage of chunks will be ignored!
     * @param bytes The byte sequence to search.
     * @return The next available offset position or -1 when nothing was found.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws RangeOutOfBoundsException This exception will be thrown when a value is not in the correct range.
     */
    public long searchNextByteSequence (byte[] bytes)  throws ClosedException, InvalidFileModeException, IOException, RangeOutOfBoundsException {
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

        for (long offsetAt = oldOffsetPosition; offsetAt < this.getFileLength(); offsetAt += bytes.length) {
            int amountOfBytesToRead = bytes.length;

            // Reading the last bytes from the file.
            if ((offsetAt + bytes.length) > this.getFileLength()) {
                offsetAt -= bytes.length;
                amountOfBytesToRead = (int) this.getRemainingFileBytes() + bytes.length;
            }

            this.setOffsetPosition(offsetAt);
            byte[] fileBytes = this.readBytes(amountOfBytesToRead);

            for (int byteIndex = 0; byteIndex < amountOfBytesToRead; byteIndex ++) {
                if (byteIndex > this.getFileLength())
                    break;

                boolean valid = true;
                for (int searchIndex = 0; searchIndex < bytes.length; searchIndex ++) {
                    if (fileBytes[byteIndex + searchIndex] != bytes[searchIndex]) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    this.setOffsetPosition(oldOffsetPosition);
                    return (offsetAt + byteIndex);
                }

                // This is to prevent an out-of-bounds exception if the number of bytes to read is equal to the bytes'
                // length.
                if (amountOfBytesToRead == bytes.length)
                    break;
            }
        }

        this.setOffsetPosition(oldOffsetPosition);

        if (wasChunkUsageEnabled)
            this.setChunkUsage(true);

        return -1;
    }

    /**
     * This method returns the state if padding is enabled or disabled.
     * @return The padding state.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public boolean isPaddingEnabled () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        return this.paddingEnabled;
    }

    /**
     * This method returns the used padding byte.
     * @return The padding byte. (It is -1 when the padding is disabled)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public byte getPaddingByte () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        return (this.paddingEnabled) ? (this.paddingByte) : (-1);
    }

    /**
     * This method returns the used padding chunk length.
     * @return The padding length. (It is -1 when the padding is disabled)
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     * @throws InvalidFileModeException This exception will be thrown when for the {@link BinaryFile} the {@link FileMode} is invalid.
     */
    public int getPaddingChunkLength () throws ClosedException, InvalidFileModeException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        if (this.getFileMode().equals(FileMode.WRITE))
            throw new InvalidFileModeException("Insufficient permissions to access the write methods in the READ mode!");

        return (this.paddingEnabled) ? (this.paddingChunkLength) : (-1);
    }

    /**
     * This method returns the remaining amount of bytes until the file ends.
     * @return The remaining bytes until the end of the file.
     * @throws ClosedException This exception will be thrown when the {@link BinaryFile} is tried to be accessed while it's closed.
     */
    public long getRemainingFileBytes () throws ClosedException {
        if (this.isClosed())
            throw new ClosedException("The BinaryFile is closed!");

        return this.getFileLength() - this.getFileOffsetPosition();
    }
}