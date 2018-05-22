package com.royale.titans.hera.crypto.sodium;

import ove.crypto.digest.Blake2b;

import java.security.SecureRandom;

public class Nonce {
    private byte[] bytes;

    public Nonce(byte[] clientKey, byte[] bytes, byte[] serverKey) {
        if (clientKey == null) {
            if (bytes == null) {
                this.bytes = new byte[24];
                new SecureRandom().nextBytes(this.bytes);
            } else {
                this.bytes = bytes;
            }
        } else {
            Blake2b b = Blake2b.Digest.newInstance(24);
            if (bytes != null) {
                b.update(bytes);
            }

            b.update(clientKey);
            b.update(serverKey);

            this.bytes = b.digest();
        }
    }

    public Nonce(byte[] clientKey, byte[] serverKey) {
        this(clientKey, null, serverKey);
    }

    public Nonce(byte[] bytes) {
        this(null, bytes, null);
    }

    public Nonce(Nonce nonce) {
        this(null, nonce.getBytes(), null);
    }

    public Nonce() {
        this(null, null, null);
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void increment() {
        short value = (short) (((bytes[0] << 8) | bytes[1]) + 2);
        bytes[0] = (byte) (value >> 8);
        bytes[1] = (byte) value;
    }
}
