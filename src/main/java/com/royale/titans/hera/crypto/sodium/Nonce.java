package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.logic.enums.ExceptionType;
import com.royale.titans.hera.logic.slots.Exceptions;
import ove.crypto.digest.Blake2b;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;

public class Nonce {
    private byte[] bytes;

    public Nonce() {
        bytes = new byte[24];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
    }

    public Nonce(byte[] nonce) {
        if (nonce.length != 24) {
            Exceptions.throwException("nonce.length must be 24", ExceptionType.NONCE);
        }

        bytes = nonce;
    }

    public Nonce(byte[] clientKey, byte[] serverKey) {
        this(clientKey, serverKey, null);
    }

    public Nonce(byte[] clientKey, byte[] serverKey, byte[] nonce) {
        final Blake2b hash = Blake2b.Digest.newInstance(24);
        if (nonce != null) {
            hash.update(nonce);
        }

        hash.update(clientKey);
        hash.update(serverKey);

        bytes = hash.digest();
    }

    public void increment() {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        short val = buffer.getShort(0);
        val += 2;
        buffer.putShort(0, val);

        bytes[0] = buffer.get(0);
        bytes[1] = buffer.get(1);
    }

    public byte[] getBytes() {
        return bytes;
    }
}
