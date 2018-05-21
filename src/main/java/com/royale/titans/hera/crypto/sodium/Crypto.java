package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.logic.enums.ExceptionType;
import com.royale.titans.hera.logic.slots.Exceptions;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

public abstract class Crypto {
    protected byte[] mPrivateKey = new byte[TweetNaCl.SIGN_PUBLIC_KEY_BYTES];
    protected byte[] mServerKey;
    protected byte[] mClientKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
    protected byte[] mSharedKey;
    protected Nonce mDecryptNonce = new Nonce();
    protected Nonce mEncryptNonce = new Nonce();
    protected byte[] mSessionKey;

    public byte[] getSessionKey() {
        return mSessionKey;
    }

    public void setSessionKey(byte[] sessionKey) {
        mSessionKey = sessionKey;
    }

    public byte[] getSharedKey() {
        return mSharedKey;
    }

    public void setSharedKey(byte[] sharedKey) {
        if (sharedKey.length != 32) {
            Exceptions.throwException("sharedKey.length must be 32", ExceptionType.CRYPTO);
        }

        mSharedKey = sharedKey;
    }

    public ByteStream encrypt(byte[] message) {
        return encrypt(message, null);
    }

    public ByteStream encrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            mEncryptNonce.increment();
            nonce = mEncryptNonce;
        }

        return ByteStream.wrap(TweetNaCl.secretbox(message, nonce.getBytes(), mSharedKey));
    }

    public ByteStream decrypt(byte[] message) {
        return decrypt(message, null);
    }

    public ByteStream decrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            mDecryptNonce.increment();
            nonce = mDecryptNonce;
        }

        return ByteStream.wrap(TweetNaCl.secretbox_open(message, nonce.getBytes(), mSharedKey));
    }

    public abstract ByteStream decryptPacket(short id, byte[] message);

    public abstract ByteStream encryptPacket(PiranhaMessage message);
}
