package club.psychose.testsuite.ibo.testcases.io;

import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.io.BinaryFile;
import club.psychose.library.ibo.enums.FileMode;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.InvalidFileModeException;
import club.psychose.library.ibo.exceptions.OpenedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;
import club.psychose.testsuite.ibo.testcases.Test;
import club.psychose.testsuite.ibo.utils.PathUtils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TC0014BinaryFileClone extends Test {
    public TC0014BinaryFileClone () {
        super("TC_0014_BINARYFILE_CLONE");
    }

    @Override
    public void executeTestCase () {
        Path filePath = PathUtils.getTestSuiteFolderPath("\\test.bin");

        try {
            this.createTestFile(filePath);
        } catch (Exception exception) {
            this.failed("TEST_FILE_CREATION_FAILED");
            exception.printStackTrace();
            return;
        }

        BinaryFile testBinaryFile;
        try {
            testBinaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            testBinaryFile.open(filePath, 0x4, FileMode.READ);
        } catch (Exception exception) {
            this.failed("TEST_FILE_OPENING_FAILED");
            exception.printStackTrace();
            return;
        }

        BinaryFile clonedBinaryFile;
        try {
            clonedBinaryFile = testBinaryFile.clone();
        } catch (Exception exception) {
            this.failed("OBJECT_CLONING_FAILED");
            exception.printStackTrace();
            return;
        }

        try {
            clonedBinaryFile.setOffsetPosition(0x0);

            if (testBinaryFile.getFileOffsetPosition() == clonedBinaryFile.getFileOffsetPosition()) {
                this.failed("FIRST_OFFSET_POSITION_IS_EQUAL");
                return;
            }

            UInt32 test1UInt32 = clonedBinaryFile.readUInt32();
            UInt32 test2UInt32 = testBinaryFile.readUInt32();

            if (test1UInt32.getValue() != 0x0) {
                this.failed("FIRST_R0EAD_TEST_FAILED");
                return;
            }

            if (test2UInt32.getValue() != 0x1) {
                this.failed("SECOND_READ_TEST_FAILED");
                return;
            }

            if (testBinaryFile.getFileOffsetPosition() == clonedBinaryFile.getFileOffsetPosition()) {
                this.failed("SECOND_OFFSET_POSITION_IS_EQUAL");
                return;
            }
        } catch (Exception exception) {
            this.failed("CLONE_TESTS_FAILED");
            exception.printStackTrace();
            return;
        }

        try {
            testBinaryFile.close();
            clonedBinaryFile.close();
            Files.deleteIfExists(filePath);
        } catch (Exception exception) {
            this.failed("CLEAN_UP_FAILED");
            exception.printStackTrace();
        }

        this.passed();
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