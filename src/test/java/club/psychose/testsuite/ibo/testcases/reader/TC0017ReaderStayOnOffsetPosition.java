package club.psychose.testsuite.ibo.testcases.reader;

import club.psychose.library.ibo.core.io.reader.FileBinaryReader;
import club.psychose.library.ibo.core.io.reader.MemoryBinaryReader;
import club.psychose.library.ibo.core.io.writer.BinaryWriter;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;

public final class TC0017ReaderStayOnOffsetPosition extends Test {
    public TC0017ReaderStayOnOffsetPosition () {
        super("TC_0016_READER_HEXVALUESEARCH_INONECHUNK_FILEBINARYREADER");
    }

    @Override
    public void executeTestCase () {
        if (this.createTestFile()) {
            try {
                FileBinaryReader fileBinaryReader = new FileBinaryReader(0x1);
                fileBinaryReader.open(PathUtils.getTestSuiteFolderPath("\\test.bin"));
                fileBinaryReader.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                fileBinaryReader.setStayOnOffsetPosition(true);

                fileBinaryReader.readUInt8();

                if (fileBinaryReader.getFileOffsetPosition() != 0x00) {
                    fileBinaryReader.close();
                    this.failed("FIRST_OFFSET_POSITION_INVALID");
                    return;
                }

                fileBinaryReader.skipOffsetPosition(0x02);

                if (fileBinaryReader.getFileOffsetPosition() != 0x0) {
                    fileBinaryReader.close();
                    this.failed("SECOND_OFFSET_POSITION_INVALID");
                    return;
                }

                fileBinaryReader.setStayOnOffsetPosition(false);
                fileBinaryReader.readInt8();

                if (fileBinaryReader.getFileOffsetPosition() != 0x1) {
                    fileBinaryReader.close();
                    this.failed("THIRD_OFFSET_POSITION_INVALID");
                    return;
                }

                fileBinaryReader.close();
                Files.deleteIfExists(PathUtils.getTestSuiteFolderPath("\\test.bin"));
            } catch (OpenedException openedException) {
                this.failed("OPENED_EXCEPTION");
                openedException.printStackTrace();
                return;
            } catch (ClosedException closedException) {
                this.failed("CLOSED_EXCEPTION");
                closedException.printStackTrace();
                return;
            } catch (IOException ioException) {
                this.failed("IO_EXCEPTION");
                ioException.printStackTrace();
                return;
            } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
                this.failed("RANGE_OUT_OF_BOUNDS");
                rangeOutOfBoundsException.printStackTrace();
                return;
            }
        }

        byte[] bytes = this.setupByteArray();
        MemoryBinaryReader memoryBinaryReader = new MemoryBinaryReader(ByteOrder.LITTLE_ENDIAN);

        try {
            memoryBinaryReader.open(bytes);
            memoryBinaryReader.setStayOnOffsetPosition(true);

            if (memoryBinaryReader.getBinaryLength() != 0x4) {
                this.failed("INVALID_BINARY_LENGTH");
                return;
            }

            memoryBinaryReader.readInt32();

            if (memoryBinaryReader.getOffsetPosition() != 0x0) {
                this.failed("FIRST_OFFSET_POSITION_INVALID");
                return;
            }

            memoryBinaryReader.setStayOnOffsetPosition(false);
            memoryBinaryReader.setOffsetPosition(0x04);

            if (memoryBinaryReader.getOffsetPosition() != 0x4) {
                this.failed("SECOND_OFFSET_POSITION_INVALID");
                return;
            }

            this.passed();
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            this.failed("RANGE_OUT_OF_BOUNDS_EXCEPTION");
            rangeOutOfBoundsException.printStackTrace();
        } finally {
            memoryBinaryReader.close();
        }
    }

    private boolean createTestFile () {
        BinaryWriter binaryWriter = new BinaryWriter(ByteOrder.BIG_ENDIAN);

        try {
            binaryWriter.open(PathUtils.getTestSuiteFolderPath("\\test.bin"), true);

            if (binaryWriter.isClosed()) {
                this.failed("BINARY_WRITER_CLOSED");
                return false;
            }

            binaryWriter.write((byte) 0x00);
            binaryWriter.write((byte) 0x01);
            binaryWriter.write((byte) 0x02);
            binaryWriter.write((byte) 0x03);

            long fileLength = binaryWriter.getFileLength();
            binaryWriter.close();

            if (fileLength != 0x04) {
                this.failed("FILE_LENGTH_INVALID");
                return false;
            }
        } catch (ClosedException closedException) {
            this.failed("CLOSED_EXCEPTION");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            this.failed("OPENED_EXCEPTION");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            this.failed("IO_EXCEPTION");
            ioException.printStackTrace();
        }

        return true;
    }

    private byte[] setupByteArray () {
        byte[] memoryBytes = new byte[0x04];

        memoryBytes[0] = 0x00;
        memoryBytes[1] = 0x00;
        memoryBytes[2] = 0x02;
        memoryBytes[3] = 0x06;

        return memoryBytes;
    }
}