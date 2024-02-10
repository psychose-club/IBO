package club.psychose.testsuite.ibo.testcases.io;

import club.psychose.library.ibo.core.datatypes.types.signed.*;
import club.psychose.library.ibo.core.datatypes.types.unsigned.*;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.utils.PathUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public final class TC0009BinaryFile {
    @Test
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

            // Check if nothing will be thrown when close is called without an open called before.
            binaryFile.close();

            // Try to access a method without that the file is opened.
            assertThrows(ClosedException.class, binaryFile::getRemainingFileBytes);

            // Open + close.
            binaryFile.open(filePath, 0x0);
            binaryFile.close();

            // Check if range is out of bounds when a position is called that didn't exist.
            assertThrows(RangeOutOfBoundsException.class, () -> binaryFile.open(filePath, 0x02));

            // Check if invalid file mode exception is thrown when we try to access write methods in the read mode.
            binaryFile.open(filePath, 0x0, FileMode.READ);
            assertThrows(InvalidFileModeException.class, () -> binaryFile.write(0x0));
            binaryFile.close();

            // Check if invalid file mode exception is thrown when we try to access read methods in the written mode.
            binaryFile.open(filePath, 0x0, FileMode.WRITE);
            assertThrows(InvalidFileModeException.class, () -> binaryFile.readBytes(1));
            binaryFile.close();

            // Check if an io exception is thrown when we try to access chunk methods without enabling the chunk mode.
            binaryFile.open(filePath, 0x0, FileMode.READ_AND_WRITE);
            assertThrows(IOException.class, binaryFile::updateChunk);

            // Check if every write method works without any issues.
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

            assertEquals(binaryFile.getFileOffsetPosition(), 0x41);

            // Enabling padding
            binaryFile.enablePadding(0x10, (byte) 0xFF);
            binaryFile.write((byte) 0x43);
            assertEquals(binaryFile.getFileOffsetPosition(), 0x51);

            binaryFile.fill((byte) 0x2A, 2);
            assertEquals(binaryFile.getFileOffsetPosition(), 0x61);

            binaryFile.fillWithoutPadding((byte) 0x31, 0x05);
            assertEquals(binaryFile.getFileOffsetPosition(), 0x66);

            binaryFile.disablePadding();
            binaryFile.write(new Int8(12));
            assertEquals(binaryFile.getFileOffsetPosition(), 0x67);

            // Check if every read method works without any issues.
            binaryFile.setOffsetPosition(0x1);
            assertEquals(binaryFile.readBytes(1)[0], 0x54);

            binaryFile.setOffsetPosition(0x0);
            assertEquals(binaryFile.readBytes(1)[0], 0x9);

            binaryFile.skipOffsetPosition(1);
            assertEquals(binaryFile.readInt8().getValue(), (short) 0x0);

            binaryFile.setOffsetPosition(0x0);
            {
                byte[] bytes = binaryFile.readBytes(2);
                assertEquals(bytes[0], 0x9);
                assertEquals(bytes[1], 0x54);
            }

            assertEquals(binaryFile.readInt8().getValue(), (short) 0x0);
            assertEquals(binaryFile.readUInt8().getValue(), (short) 0x1);
            assertEquals(binaryFile.readInt16().getValue(), 0x2);
            assertEquals(binaryFile.readUInt16().getValue(), 0x3);
            assertEquals(binaryFile.readInt32().getValue(), 0x4);

            binaryFile.setStayOnOffsetPosition(true);
            assertEquals(binaryFile.readUInt32().getValue(), 0x5);
            assertEquals(binaryFile.readUInt32().getValue(), 0x5);
            assertEquals(binaryFile.readUInt32().getValue(), 0x5);
            binaryFile.setStayOnOffsetPosition(false);

            assertEquals(binaryFile.readUInt32().getValue(), 0x5);
            assertEquals(binaryFile.readInt64().getValue().intValue(), 0x6);

            binaryFile.setStayOnOffsetPosition(true);
            assertEquals(binaryFile.readUInt64().getValue().intValue(), 0x7);
            assertEquals(binaryFile.readUInt64().getValue().intValue(), 0x7);
            assertEquals(binaryFile.readUInt64().getValue().intValue(), 0x7);
            binaryFile.setStayOnOffsetPosition(false);

            assertEquals(binaryFile.readUInt64().getValue().intValue(), 0x7);
            assertEquals(binaryFile.readInt8().getValue().intValue(), 0x0); // Byte = Int8
            assertEquals(binaryFile.readFloat(), 3.4f);
            assertEquals(binaryFile.readDouble(), 4.5);
            assertEquals(binaryFile.readString(14), "LoveeYou<3<3<3");
            assertEquals(binaryFile.readInt24().getValue(), 0x8);
            assertEquals(binaryFile.readUInt24().getValue(), 0x9);
            assertEquals(binaryFile.getFileOffsetPosition(), 0x41);

            // Check if an io exception is thrown when we try to enable the chunk mode without setting a specified chunk length.
            binaryFile.setOffsetPosition(0x0);
            assertThrows(IOException.class, () -> binaryFile.setChunkUsage(true));

            binaryFile.setChunkLength(0x10);
            binaryFile.setChunkUsage(true);

            assertEquals(binaryFile.readBytes(1)[0], 0x9);

            binaryFile.setOffsetPosition(0x8);
            assertEquals(binaryFile.readInt32().getValue(), 0x4); // Reading bytes from two chunks while reading the value.

            byte[] sequence1 = new byte[3];
            sequence1[0] = (byte) 0xFF;
            sequence1[1] = (byte) 0xFF;
            sequence1[2] = (byte) 0xFF;

            long sequence1Offset = binaryFile.searchNextByteSequence(sequence1);
            assertEquals(sequence1Offset, 0x42);

            binaryFile.disablePadding();
            String sequence2 = "2AFF";
            long sequence2Offset = binaryFile.searchNextHEXValue(sequence2);

            assertEquals(sequence2Offset, 0x52);
            assertTrue(binaryFile.isChunkUsageEnabled());

            // Cleanup part.
            binaryFile.setChunkUsage(false);
            binaryFile.close();
            Files.deleteIfExists(filePath);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
        }
    }
}