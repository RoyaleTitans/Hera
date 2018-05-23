package com.royale.titans.hera.crypto.sodium.nacl;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;
import com.royale.titans.hera.utils.Debugger;

import java.util.Arrays;

public class PublicBox {
    private static int KEYBYTES = 32;
    private static int NONCEBYTES = 24;
    private static int ZEROBYTES = 32;
    private static int BOXZEROBYTES = 16;
    private static int BEFORENMBYTES = 32;

    private byte[] PrecomputedSharedKey = new byte[BEFORENMBYTES];

    public PublicBox(byte[] privatekey, byte[] publickey) {
        curve25519xsalsa20poly1305.crypto_box_beforenm(this.PrecomputedSharedKey, publickey, privatekey);
    }

    public byte[] create(byte[] plain, byte[] nonce) {
        int plainLength = plain.length;
        byte[] paddedbuffer = new byte[plainLength + ZEROBYTES];
        Arrays.copyOfRange(paddedbuffer, ZEROBYTES, plainLength);

        if (curve25519xsalsa20poly1305.crypto_box_afternm(paddedbuffer, paddedbuffer, paddedbuffer.length, nonce, this.PrecomputedSharedKey) != 0)
            Debugger.error("PublicBox Encryption failed");

        byte[] output = new byte[plainLength + BOXZEROBYTES];
        Arrays.copyOfRange(paddedbuffer, ZEROBYTES - BOXZEROBYTES, output.length);
        return output;
    }

    public byte[] open(byte[] cipher, byte[] nonce) {
        int cipherLength = cipher.length;
        byte[] paddedbuffer = new byte[cipherLength + BOXZEROBYTES];
        Arrays.copyOfRange(paddedbuffer, BOXZEROBYTES, cipherLength);

        if (curve25519xsalsa20poly1305.crypto_box_afternm(paddedbuffer, paddedbuffer, paddedbuffer.length, nonce, this.PrecomputedSharedKey) != 0)
            Debugger.error("PublicBox Decryption failed");

        byte[] output = new byte[paddedbuffer.length - ZEROBYTES];
        Arrays.copyOfRange(paddedbuffer, ZEROBYTES, output.length);
        return output;
    }
}
