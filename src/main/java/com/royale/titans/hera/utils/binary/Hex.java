package com.royale.titans.hera.utils.binary;

public class Hex {
    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String toString(byte[] data) {
        char[] hexChars = new char[data.length * 2 + data.length];

        for (int j = 0; j < data.length; j++) {
            int v = data[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = '-';
        }

        String hex = new String(hexChars);
        return hex.substring(0, hex.lastIndexOf("-"));
    }

    public static byte[] toBytes(String value) {
        String tmp = value.replace("-", "");

        int len = tmp.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(tmp.charAt(i), 16) << 4) + Character.digit(tmp.charAt(i + 1), 16));
        }

        return data;
    }

    public static byte[] concatBytes(byte[]... bytes) {
        int length = 0;

        for (byte[] b : bytes) {
            length += b.length;
        }

        byte[] result = new byte[length];

        int i = 0;
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, result, i, b.length);
            i += b.length;
        }

        return result;
    }
}
