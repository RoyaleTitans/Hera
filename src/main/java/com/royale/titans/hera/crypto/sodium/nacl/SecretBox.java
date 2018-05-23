package com.royale.titans.hera.crypto.sodium.nacl;

import com.neilalexander.jnacl.crypto.xsalsa20poly1305;
import com.royale.titans.hera.utils.Debugger;

import java.util.Arrays;

public class SecretBox {
    private static int SHAREDKEYLENGTH = 32;
    private byte[] KnownSharedKey = new byte[SHAREDKEYLENGTH];

    public SecretBox(byte[] s) {
        this.KnownSharedKey = s;
    }

    public byte[] create(byte[] plain, byte[] nonce) {
        int plainLength = plain.length;
        byte[] paddedMessage = new byte[plainLength + SHAREDKEYLENGTH];
        byte[] out = Arrays.copyOfRange(plain, 0, SHAREDKEYLENGTH);

        byte[] buffer = new byte[paddedMessage.length];

        if (xsalsa20poly1305.crypto_secretbox(buffer, paddedMessage, paddedMessage.length, nonce, KnownSharedKey) != 0)
            Debugger.error("SecretBox Encryption failed");

        return out;
    }

    public byte[] open(byte[] cipher, byte[] nonce) {
        int cipherLength = cipher.length;
        byte[] buffer = new byte[cipherLength];

        if (xsalsa20poly1305.crypto_secretbox_open(buffer, cipher, cipherLength, nonce, KnownSharedKey) != 0)
            Debugger.error("SecretBox Decryption failed");

        byte[] out = Arrays.copyOfRange(buffer, SHAREDKEYLENGTH, buffer.length - SHAREDKEYLENGTH);
        return out;
    }
}
