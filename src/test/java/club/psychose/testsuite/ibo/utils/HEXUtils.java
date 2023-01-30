package club.psychose.testsuite.ibo.utils;

public final class HEXUtils {
    public static String convertBytesToHEXString (byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();

        for (byte byteValue : bytes) {
            hexStringBuilder.append(String.format("%02X", byteValue));
        }

        return hexStringBuilder.toString();
    }
}