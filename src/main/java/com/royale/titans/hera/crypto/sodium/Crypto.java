package com.royale.titans.hera.crypto.sodium;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;
import com.neilalexander.jnacl.crypto.xsalsa20poly1305;

import java.util.Arrays;

public abstract class Crypto {
    protected int state = 0;
    protected byte[] privateKey, serverKey, clientKey, sharedKey, sessionKey;
    protected Nonce decryptNonce, encryptNonce;

    public Crypto(byte[] sk) {
        serverKey = sk;
        clientKey = new byte[32];
        privateKey = new byte[32];
    }

    public byte[] getSharedKey() {
        return this.sharedKey;
    }

    public void setSharedKey(byte[] sharedKey) {
        this.sharedKey = sharedKey;
    }

    public Nonce getEncryptNonce() {
        return this.encryptNonce;
    }

    protected void setEncryptNonce(Nonce encryptNonce) {
        this.encryptNonce = encryptNonce;
    }

    public Nonce getDecryptNonce() {
        return this.decryptNonce;
    }

    protected void setDecryptNonce(Nonce decryptNonce) {
        this.decryptNonce = encryptNonce;
    }

    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = sessionKey;
    }

    public byte[] getSessionKey() {
        return this.sessionKey;
    }

    protected void beforeNm(byte[] serverKey) {
        this.sharedKey = new byte[32];
        curve25519xsalsa20poly1305.crypto_box_beforenm(sharedKey, serverKey, privateKey);
    }

    public byte[] encrypt(byte[] data, Nonce encryptNonce) {
        if (encryptNonce == null) {
            this.encryptNonce.increment();
            encryptNonce = this.encryptNonce;
        }
        byte[] paddedData = new byte[32 + data.length];
        byte[] encryptedData = new byte[32 + data.length];
        System.arraycopy(data, 0, paddedData, 32, data.length);
        xsalsa20poly1305.crypto_secretbox(encryptedData, paddedData, paddedData.length, encryptNonce.getBytes(),
                sharedKey);
        return Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
    }

    public byte[] decrypt(byte[] data, Nonce decryptNonce) {
        if (decryptNonce == null) {
            this.decryptNonce.increment();
            decryptNonce = this.decryptNonce;
        }

        byte[] c = new byte[16 + data.length];
        byte[] m = new byte[c.length];
        System.arraycopy(data, 0, c, 16, data.length);
        if (xsalsa20poly1305.crypto_secretbox_open(m, c, c.length, decryptNonce.getBytes(), sharedKey) != 0)
            return null;
        return Arrays.copyOfRange(m, 32, m.length);
    }

    public int getState() {
        return state;
    }
}
