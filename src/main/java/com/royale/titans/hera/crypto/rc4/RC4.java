package com.royale.titans.hera.crypto.rc4;

import com.royale.titans.hera.logic.enums.ExceptionType;
import com.royale.titans.hera.logic.slots.Exceptions;

public class RC4 {
    private final byte[] s = new byte[256];
    private final byte[] t = new byte[256];
    private int keyLength;

    public RC4(String key) {
        this(stringToByteArray(key));
    }

    private RC4(final byte[] key) {
        if (key.length < 1 || key.length > 256) {
            Exceptions.throwException("key must be between 1 and 256 bytes", ExceptionType.CRYPTO);
        } else {
            keyLength = key.length;
            for (int i = 0; i < 256; i++) {
                s[i] = (byte) i;
                t[i] = key[i % keyLength];
            }

            int j = 0;
            byte tmp;
            for (int i = 0; i < 256; i++) {
                j = (j + s[i] + t[i]) & 0xFF;
                tmp = s[j];
                s[j] = s[i];
                s[i] = tmp;
            }
        }
    }

    public byte[] encrypt(final byte[] plainText) {
        final byte[] cipherText = new byte[plainText.length];
        int i = 0, j = 0, k, t;
        byte tmp;

        for (int counter = 0; counter < plainText.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + s[i]) & 0xFF;
            tmp = s[j];
            s[j] = s[i];
            s[i] = tmp;
            t = (s[i] + s[j]) & 0xFF;
            k = s[t];
            cipherText[counter] = (byte) (plainText[counter] ^ k);
        }

        return cipherText;
    }

    public byte[] decrypt(final byte[] cipherText) {
        return encrypt(cipherText);
    }

    private static byte[] stringToByteArray(String str) {
        byte[] bytes = new byte[str.length()];

        for (int i = 0; i < str.length(); i++)
            bytes[i] = (byte) str.charAt(i);

        return bytes;
    }
}
