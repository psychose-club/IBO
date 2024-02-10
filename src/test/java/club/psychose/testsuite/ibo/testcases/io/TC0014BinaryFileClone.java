package club.psychose.testsuite.ibo.testcases.io;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.utils.PathUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class TC0014BinaryFileClone {
    @Test
    public void executeTestCase () {
        Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");

        try {
            this.createTestFile(filePath);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        BinaryFile testBinaryFile;
        try {
            testBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            testBinaryFile.open(filePath, 0x4, FileMode.READ);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        BinaryFile clonedBinaryFile;
        try {
            clonedBinaryFile = testBinaryFile.clone();
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            clonedBinaryFile.setOffsetPosition(0x0);
            assertEquals(testBinaryFile.getFileOffsetPosition(), clonedBinaryFile.getFileOffsetPosition());

            assertEquals(clonedBinaryFile.readUInt32().getValue(), 0x0);
            assertEquals(testBinaryFile.readUInt32().getValue(), 0x1);

            assertEquals(testBinaryFile.getFileOffsetPosition(), clonedBinaryFile.getFileOffsetPosition());
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.close();
            clonedBinaryFile.close();
            Files.deleteIfExists(filePath);
        } catch (Exception exception) {
            fail("An exception occurred while executing the testcase!");
            exception.printStackTrace();
        }
    }

    private void createTestFile (Path filePath) throws ClosedException, InvalidFileModeException, IOException, OpenedException, RangeOutOfBoundsException {
        // Setup part.
        Files.deleteIfExists(filePath);

        if (!(Files.exists(PathUtils.getTestSuiteFolderPath(null))))
            Files.createDirectories(PathUtils.getTestSuiteFolderPath(null));

        Files.createFile(filePath);

        // Writing sample data.
        BinaryFile testBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
        testBinaryFile.open(filePath, 0x0, FileMode.WRITE);
        testBinaryFile.write(new UInt32(0x0));
        testBinaryFile.write(new UInt32(0x1));
        testBinaryFile.write(new UInt32(0x2));
        testBinaryFile.write(new UInt32(0x3));
        testBinaryFile.write(new UInt32(0x4));
        testBinaryFile.write(new UInt32(0x5));
        testBinaryFile.write(new UInt32(0x6));
        testBinaryFile.write(new UInt32(0x7));
        testBinaryFile.write(new UInt32(0x8));
        testBinaryFile.write(new UInt32(0x9));
        testBinaryFile.close();
    }
}