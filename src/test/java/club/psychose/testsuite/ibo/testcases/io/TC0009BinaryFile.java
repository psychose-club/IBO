package club.psychose.testsuite.ibo.testcases.io;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TC0009BinaryFile extends Test {
    public TC0009BinaryFile () {
        super("TC_0009_BINARYFILE");
    }

    @Override
    public void executeTestCase () {
        try {
            // Setup part.
            Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");
            Files.deleteIfExists(filePath);

            if (!(Files.exists(PathUtils.getTestSuiteFolderPath(null))))
                Files.createDirectories(PathUtils.getTestSuiteFolderPath(null));

            Files.createFile(filePath);

            // Test part.
            BinaryFile binaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);

            // Check if nothing will be thrown when close is called without open called before.
            binaryFile.close();

            // Try to access a method without that the file is opened.
            try {
                binaryFile.getRemainingFileBytes();
                this.failed("ACCESSED_METHOD_WITHOUT_OPENED_FILE"); // <- This shouldn't happen.
                return;
            } catch (ClosedException ignored) {
            }

            // Open + close.
            binaryFile.open(filePath, 0x0);
            binaryFile.close();

            // Check if range is out of bounds when a position is called that didn't exist.
            try {
                binaryFile.open(filePath, 0x2);
                this.failed("POSITION_OUT_OF_BOUNDS_OPEN");
                return;
            } catch (RangeOutOfBoundsException ignored) {
            }

            // Check if invalid file mode exception is thrown when we try to access write methods in the read mode.
            binaryFile.open(filePath, 0x0, FileMode.READ);

            try {
                binaryFile.write(0x0);
                this.failed("INVALID_FILE_MODE_1");
                return;
            } catch (InvalidFileModeException ignored) {
            }

            binaryFile.close();

            // Check if invalid file mode exception is thrown when we try to access read methods in the write mode.
            binaryFile.open(filePath, 0x0, FileMode.WRITE);

            try {
                binaryFile.readBytes(1);
                this.failed("INVALID_FILE_MODE_2");
                return;
            } catch (InvalidFileModeException ignored) {
            }

            binaryFile.close();
            // Check if an io exception is thrown when we try to access chunk methods without enabling the chunk mode.
            binaryFile.open(filePath, 0x0, FileMode.READ_AND_WRITE);

            try {
                binaryFile.updateChunk();
                this.failed("INVALID_FILE_MODE_3");
                return;
            } catch (IOException ignored) {
            }

            // Check if every written method works without any issues.
            {
                byte[] bytes = new byte[2];
                bytes[0] = 0x9;
                bytes[1] = 0x54;

                binaryFile.write(bytes);
            }

            binaryFile.write(new Int8(0));
            binaryFile.write(new UInt8(1));
            binaryFile.write(new Int16(2));
            binaryFile.write(new UInt16(3));
            binaryFile.write(new Int32(4));
            binaryFile.write(new UInt32(5));
            binaryFile.write(new Int64(6));
            binaryFile.write(new UInt64(7));
            binaryFile.write((byte) 0x0);
            binaryFile.write((float) 3.4);
            binaryFile.write(4.5);
            binaryFile.write("LoveeYou<3<3<3");
            binaryFile.write(new Int24(8));
            binaryFile.write(new UInt24(9));

            if (binaryFile.getFileOffsetPosition() != 0x41) {
                this.failed("WRITE_OPERATION_FAILED");
                return;
            }

            // Enabling padding
            binaryFile.enablePadding(0x10, (byte) 0xFF);
            binaryFile.write((byte) 0x43);

            if (binaryFile.getFileOffsetPosition() != 0x51) {
                this.failed("PADDING_OPERATION_FAILED_1");
                return;
            }

            binaryFile.fill((byte) 0x2A, 2);

            if (binaryFile.getFileOffsetPosition() != 0x61) {
                this.failed("PADDING_OPERATION_FAILED_2");
                return;
            }

            binaryFile.fillWithoutPadding((byte) 0x31, 0x05);

            if (binaryFile.getFileOffsetPosition() != 0x66) {
                this.failed("PADDING_OPERATION_FAILED_3");
                return;
            }

            binaryFile.disablePadding();
            binaryFile.write(new Int8(12));

            if (binaryFile.getFileOffsetPosition() != 0x67) {
                this.failed("PADDING_OPERATION_FAILED_4");
                return;
            }

            // Check if every read method works without any issues.
            binaryFile.setOffsetPosition(0x1);
            if (binaryFile.readBytes(1)[0] != 0x54) {
                this.failed("READ_OPERATION_FAILED_1");
                return;
            }

            binaryFile.setOffsetPosition(0x0);
            if (binaryFile.readBytes(1)[0] != 0x9) {
                this.failed("READ_OPERATION_FAILED_2");
                return;
            }

            binaryFile.skipOffsetPosition(1);
            if (binaryFile.readInt8().getValue() != 0x0) {
                this.failed("READ_OPERATION_FAILED_3");
                return;
            }

            binaryFile.setOffsetPosition(0x0);
            {
                byte[] bytes = binaryFile.readBytes(2);

                if (bytes[0] != 0x9) {
                    this.failed("READ_OPERATION_FAILED_4");
                    return;
                }

                if (bytes[1] != 0x54) {
                    this.failed("READ_OPERATION_FAILED_5");
                    return;
                }
            }

            if (binaryFile.readInt8().getValue() != 0x0) {
                this.failed("READ_OPERATION_FAILED_6");
                return;
            }

            if (binaryFile.readUInt8().getValue() != 0x1) {
                this.failed("READ_OPERATION_FAILED_7");
                return;
            }

            if (binaryFile.readInt16().getValue() != 0x2) {
                this.failed("READ_OPERATION_FAILED_8");
                return;
            }

            if (binaryFile.readUInt16().getValue() != 0x3) {
                this.failed("READ_OPERATION_FAILED_9");
                return;
            }

            if (binaryFile.readInt32().getValue() != 0x4) {
                this.failed("READ_OPERATION_FAILED_10");
                return;
            }

            binaryFile.setStayOnOffsetPosition(true);

            if (binaryFile.readUInt32().getValue() != 0x5) {
                this.failed("READ_OPERATION_FAILED_11");
                return;
            }

            if (binaryFile.readUInt32().getValue() != 0x5) {
                this.failed("READ_OPERATION_FAILED_12");
                return;
            }

            if (binaryFile.readUInt32().getValue() != 0x5) {
                this.failed("READ_OPERATION_FAILED_13");
                return;
            }

            if (binaryFile.readUInt32().getValue() != 0x5) {
                this.failed("READ_OPERATION_FAILED_14");
                return;
            }

            binaryFile.setStayOnOffsetPosition(false);

            if (binaryFile.readUInt32().getValue() != 0x5) {
                this.failed("READ_OPERATION_FAILED_15");
                return;
            }

            if (binaryFile.readInt64().getValue().intValue() != 0x6) {
                this.failed("READ_OPERATION_FAILED_16");
                return;
            }

            binaryFile.setStayOnOffsetPosition(true);

            if (binaryFile.readUInt64().getValue().intValue() != 0x7) {
                this.failed("READ_OPERATION_FAILED_17");
                return;
            }

            if (binaryFile.readUInt64().getValue().intValue() != 0x7) {
                this.failed("READ_OPERATION_FAILED_18");
                return;
            }

            if (binaryFile.readUInt64().getValue().intValue() != 0x7) {
                this.failed("READ_OPERATION_FAILED_19");
                return;
            }

            binaryFile.setStayOnOffsetPosition(false);

            if (binaryFile.readUInt64().getValue().intValue() != 0x7) {
                this.failed("READ_OPERATION_FAILED_20");
                return;
            }

            // Byte = Int8
            if (binaryFile.readInt8().getValue().intValue() != 0x0) {
                this.failed("READ_OPERATION_FAILED_21");
                return;
            }

            if (binaryFile.readFloat() != 3.4f) {
                this.failed("READ_OPERATION_FAILED_22");
                return;
            }

            if (binaryFile.readDouble() != 4.5) {
                this.failed("READ_OPERATION_FAILED_23");
                return;
            }

            if (!(binaryFile.readString(14).equals("LoveeYou<3<3<3"))) {
                this.failed("READ_OPERATION_FAILED_24");
                return;
            }

            if (binaryFile.readInt24().getValue() != 0x8) {
                this.failed("READ_OPERATION_FAILED_25");
                return;
            }

            if (binaryFile.readUInt24().getValue() != 0x9) {
                this.failed("READ_OPERATION_FAILED_26");
                return;
            }

            if (binaryFile.getFileOffsetPosition() != 0x41) {
                this.failed("READ_OPERATION_FAILED_27");
                return;
            }

            // Check if an io exception is thrown when we try to enable the chunk mode without setting a specified chunk length.
            binaryFile.setOffsetPosition(0x0);

            try {
                binaryFile.setChunkUsage(true);
                this.failed("CHUNK_OPERATION_FAILED_1");
                return;
            } catch (IOException ignored) {
            }

            binaryFile.setChunkLength(0x10);
            binaryFile.setChunkUsage(true);

            if (binaryFile.readBytes(1)[0] != 0x9) {
                this.failed("CHUNK_OPERATION_FAILED_2");
                return;
            }

            binaryFile.setOffsetPosition(0x8);

            if (binaryFile.readInt32().getValue() != 0x4) { // Reading bytes from two chunks while reading the value.
                this.failed("CHUNK_OPERATION_FAILED_3");
                return;
            }

            byte[] sequence1 = new byte[3];
            sequence1[0] = (byte) 0xFF;
            sequence1[1] = (byte) 0xFF;
            sequence1[2] = (byte) 0xFF;

            long sequence1Offset = binaryFile.searchNextByteSequence(sequence1);

            if (sequence1Offset != 0x42) {
                this.failed("CHUNK_OPERATION_FAILED_4");
                return;
            }

            binaryFile.disablePadding();

            String sequence2 = "2AFF";

            long sequence2Offset = binaryFile.searchNextHEXValue(sequence2);

            if (sequence2Offset != 0x52) {
                this.failed("CHUNK_OPERATION_FAILED_5");
                return;
            }

            if (!(binaryFile.isChunkUsageEnabled())) {
                this.failed("CHUNK_OPERATION_FAILED_6");
                return;
            }

            binaryFile.setChunkUsage(false);
            binaryFile.close();

            // Cleanup part.
            Files.deleteIfExists(filePath);
            this.passed();
        } catch (Exception exception) {
            this.failed("EXCEPTION");
            exception.printStackTrace();
        }
    }
}