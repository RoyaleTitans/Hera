package com.royale.titans.hera.crypto.sodium;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;
import com.royale.titans.hera.utils.binary.Hex;

import java.util.Arrays;

public class ClientCrypto extends Crypto {
    public ClientCrypto(byte[] serverKey) {
        super(serverKey);
        curve25519xsalsa20poly1305.crypto_box_keypair(clientKey, privateKey);
        beforeNm(serverKey);
        setEncryptNonce(new Nonce());
    }

    public byte[] encryptPacket(short id, byte[] message) {
        switch (id) {
            case 10100: {
                return message;
            }
            case 10101: {
                return encryptPacketLogin(message);
            }
            default: {
                return encrypt(message, null);
            }
        }
    }

    private byte[] encryptPacketLogin(byte[] message) {
        Nonce nonce = new Nonce(clientKey, serverKey);
        state++;
        return Hex.concatBytes(clientKey, encrypt(Hex.concatBytes(sessionKey, encryptNonce.getBytes(), message), nonce));
    }

    private byte[] decryptPacketLoginOK(byte[] message, ServerCrypto serverCrypto) {
        Nonce nonce = new Nonce(clientKey, encryptNonce.getBytes(), serverKey);
        byte[] decrypted = decrypt(message, nonce);
        decryptNonce = new Nonce(Arrays.copyOfRange(decrypted, 0, 24));
        if (serverCrypto != null)
            serverCrypto.setEncryptNonce(new Nonce(Arrays.copyOfRange(decrypted, 0, 24)));
        sharedKey = Arrays.copyOfRange(decrypted, 24, 56);
        state++;
        return Arrays.copyOfRange(decrypted, 56, decrypted.length);
    }

    public byte[] decryptPacket(short id, byte[] message) {
        switch (id) {
            case 20100:
            case 20103: {
                return message;
            }
            case 22194: {
                return decryptPacketLoginOK(message, null);
            }
            default: {
                return decrypt(message, null);
            }
        }
    }
}
