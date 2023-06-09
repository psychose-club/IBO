package club.psychose.library.ibo.exceptions;

/**
 * This exception will be thrown when for the {@link club.psychose.library.ibo.core.io.BinaryFile} the {@link club.psychose.library.ibo.enums.FileMode} is invalid.
 */

public final class InvalidFileModeException extends Exception {
    public InvalidFileModeException () {
        super();
    }

    public InvalidFileModeException (String message) {
        super(message);
    }

    public InvalidFileModeException (String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileModeException (Throwable cause) {
        super(cause);
    }
}