package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.logic.enums.ExceptionType;
import com.royale.titans.hera.logic.slots.Exceptions;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

public abstract class Crypto {
    protected byte[] privateKey = new byte[TweetNaCl.SIGN_PUBLIC_KEY_BYTES];
    protected byte[] serverKey;
    protected byte[] clientKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
    protected byte[] sharedKey;
    protected Nonce decryptNonce = new Nonce();
    protected Nonce encryptNonce = new Nonce();
    protected byte[] sessionKey;

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = sessionKey;
    }

    public byte[] getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(byte[] sharedKey) {
        if (sharedKey.length != 32) {
            Exceptions.throwException("sharedKey.length must be 32", ExceptionType.CRYPTO);
        }

        this.sharedKey = sharedKey;
    }

    public ByteStream encrypt(byte[] message) {
        return encrypt(message, null);
    }

    public ByteStream encrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            encryptNonce.increment();
            nonce = encryptNonce;
        }

        return ByteStream.wrap(TweetNaCl.secretbox(message, nonce.getBytes(), sharedKey));
    }

    public ByteStream decrypt(byte[] message) {
        return decrypt(message, null);
    }

    public ByteStream decrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            decryptNonce.increment();
            nonce = decryptNonce;
        }

        return ByteStream.wrap(TweetNaCl.secretbox_open(message, nonce.getBytes(), sharedKey));
    }

    public abstract ByteStream decryptPacket(short id, byte[] message);

    public abstract ByteStream encryptPacket(PiranhaMessage message);
}
